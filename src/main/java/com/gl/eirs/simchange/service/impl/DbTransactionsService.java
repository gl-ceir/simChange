package com.gl.eirs.simchange.service.impl;


import com.gl.eirs.simchange.builder.*;
import com.gl.eirs.simchange.config.AppConfig;
import com.gl.eirs.simchange.entity.app.*;
import com.gl.eirs.simchange.repository.app.*;
import com.gl.eirs.simchange.service.intf.IDbTransactions;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class DbTransactionsService implements IDbTransactions {

    @Autowired
    GreyListRepository greyListRepository;
    @Autowired
    GreyListHisRepository greyListHisRepository;
    @Autowired
    BlackListRepository blackListRepository;
    @Autowired
    BlackListHisRepository blackListHisRepository;
    @Autowired
    ExceptionListRepository exceptionListRepository;
    @Autowired
    ExceptionListHisRepository exceptionListHisRepository;
    @Autowired
    GreyListHisBuilder greyListHisBuilder;
    @Autowired
    BlackListHisBuilder blackListHisBuilder;
    @Autowired
    ExceptionListHisBuilder exceptionListHisBuilder;
    @Autowired
    GreyListBuilder greyListBuilder;
    @Autowired
    ExceptionListBuilder exceptionListBuilder;
    @Autowired
    BlackListBuilder blackListBuilder;
    @Autowired
    ImeiListHisRepository imeiListHisRepository;
    @Autowired
    ImeiListRepository imeiListRepository;
    @Autowired
    ImeiListBuilder imeiListBuilder;
    @Autowired
    ImeiListHisBuilder imeiListHisBuilder;

    @Autowired
    private DuplicateDeviceDetailRepository duplicateDeviceDetailRepository;
    @Autowired
    private DuplicateDeviceDetailHisBuilder duplicateDeviceDetailHisBuilder;
    @Autowired
    private DuplicateDeviceDetailHisRepository duplicateDeviceDetailHisRepository;
    @Autowired
    private DuplicateDeviceDetailBuilder duplicateDeviceDetailBuilder;

    @Autowired
    private ActiveMsisdnListRepository activeMsisdnListRepository;

    @Autowired
    private ActiveMsisdnListHisRepository activeMsisdnListHisRepository;

    @Autowired
    private ActiveMsisdnListBuilder activeMsisdnListBuilder;
    @Autowired
    private ActiveMsisdnListHisBuilder activeMsisdnListHisBuilder;


    @Override
    @Transactional
    public boolean dbTransaction(GreyList greyList, String newImsi) {

        try {

            // delete old record
            greyListRepository.deleteById(Math.toIntExact(greyList.getId()));
            // insert in his table
            GreyListHis greyListHis = greyListHisBuilder.forInsert(greyList, 0);
            greyListHisRepository.save(greyListHis);

            // insert new record in grey list
            GreyList greyList1 = greyListBuilder.forInsert(greyList, newImsi);
            greyListRepository.save(greyList1);

            // insert in his table
            greyListHis = greyListHisBuilder.forInsert(greyList1, 1);
            greyListHisRepository.save(greyListHis);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    @Override
    @Transactional
    public boolean dbTransaction(ExceptionList exceptionList, String newImsi) {

        try {

            // delete old record
            exceptionListRepository.deleteById(Math.toIntExact(exceptionList.getId()));
            // insert in his table
            ExceptionListHis exceptionListHis = exceptionListHisBuilder.forInsert(exceptionList, 0);
            exceptionListHisRepository.save(exceptionListHis);

            // insert new record in exception list
            ExceptionList exceptionList1 = exceptionListBuilder.forInsert(exceptionList, newImsi);
            exceptionListRepository.save(exceptionList1);

            // insert in history table
            exceptionListHis = exceptionListHisBuilder.forInsert(exceptionList1, 1);
            exceptionListHisRepository.save(exceptionListHis);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public boolean dbTransaction(BlackList blackList, String newImsi) {
        try {

            // delete old record
            blackListRepository.deleteById(Math.toIntExact(blackList.getId()));
            // insert in history table
            BlackListHis blackListHis = blackListHisBuilder.forInsert(blackList, 0);
            blackListHisRepository.save(blackListHis);

            // insert new record in black list
            BlackList blackList1 = blackListBuilder.forInsert(blackList, newImsi);
            blackListRepository.save(blackList1);

            // insert in history table
            blackListHis = blackListHisBuilder.forInsert(blackList1, 1);
            blackListHisRepository.save(blackListHis);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean dbTransaction(ImeiList imeiList, String newImsi) {
        try {

            // delete old record
            imeiListRepository.deleteById(Math.toIntExact(imeiList.getId()));
            // insert in history table
            ImeiListHis imeiListHis = imeiListHisBuilder.forInsert(imeiList, 0, "DELETE");
            imeiListHisRepository.save(imeiListHis);

            // insert new record in imei list
            ImeiList imeiList1 = imeiListBuilder.forInsert(imeiList, newImsi);
            imeiListRepository.save(imeiList1);

            // insert in history table
            imeiListHis = imeiListHisBuilder.forInsert(imeiList1, 1, "ADD");
            imeiListHisRepository.save(imeiListHis);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean dbTransaction(DuplicateDeviceDetail duplicateDeviceDetail, String newImsi) {

        try {

            // delete old record
            duplicateDeviceDetailRepository.deleteById(Math.toIntExact(duplicateDeviceDetail.getId()));
            // insert in his table
            DuplicateDeviceDetailHis duplicateDeviceDetailHis = duplicateDeviceDetailHisBuilder.forInsert(duplicateDeviceDetail, 0, "DELETE");
            duplicateDeviceDetailHisRepository.save(duplicateDeviceDetailHis);

            // insert new record in exception list
            DuplicateDeviceDetail duplicateDeviceDetail1 = duplicateDeviceDetailBuilder.forInsert(duplicateDeviceDetail, newImsi);
            duplicateDeviceDetailRepository.save(duplicateDeviceDetail1);

            // insert in history table
            duplicateDeviceDetailHis = duplicateDeviceDetailHisBuilder.forInsert(duplicateDeviceDetail1, 1, "ADD");
            duplicateDeviceDetailHisRepository.save(duplicateDeviceDetailHis);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public boolean dbTransaction(ActiveMsisdnList activeMsisdnList, String newImsi) {
        try {
            // Check if the entry with oldImsi exists
            activeMsisdnListRepository.deleteById(activeMsisdnList.getId());

            ActiveMsisdnList activeMsisdnList1 = activeMsisdnListBuilder.forInsert(activeMsisdnList, newImsi);
            activeMsisdnListRepository.save(activeMsisdnList1);

                // Create a history entry
                ActiveMsisdnListHis activeMsisdnListHis = activeMsisdnListHisBuilder
                        .forInsert(activeMsisdnList, 0);

                // Insert the history entry into active_msisdn_list_his
                activeMsisdnListHisRepository.save(activeMsisdnListHis);

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
