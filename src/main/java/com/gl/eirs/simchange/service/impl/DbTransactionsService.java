package com.gl.eirs.simchange.service.impl;


import com.gl.eirs.simchange.builder.*;
import com.gl.eirs.simchange.entity.app.*;
import com.gl.eirs.simchange.repository.app.*;
import com.gl.eirs.simchange.service.intf.IDbTransactions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
