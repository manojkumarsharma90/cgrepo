package com.cg.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Account;
import com.cg.exception.AccountNotFoundException;
import com.cg.exception.InsuficientFundException;
import com.cg.repo.AccountRepo;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepo accRepo;
	
	@Override
	public boolean transferFund(Integer fromID, Integer to, Double amount) {
		Optional<Account> optFrom =accRepo.findById(fromID);
		
		if(optFrom.isEmpty()) {
			throw new AccountNotFoundException("Account not found for "+fromID);
		}
		
		Optional<Account> optTo =accRepo.findById(to);
		if(optTo.isEmpty()) {
			throw new AccountNotFoundException("Account not found for "+to);
		}
		
		
		Account fromAcc=optFrom.get();
		Account toAcc=optTo.get();
		
		if(fromAcc.getAmt()<amount) {
			throw new InsuficientFundException("fund is low");
		}
		fromAcc.setAmt(fromAcc.getAmt()-amount);
		toAcc.setAmt(toAcc.getAmt()+amount);
		
		accRepo.save(fromAcc);
		accRepo.save(toAcc);
		
		return true;
		
	}

}
