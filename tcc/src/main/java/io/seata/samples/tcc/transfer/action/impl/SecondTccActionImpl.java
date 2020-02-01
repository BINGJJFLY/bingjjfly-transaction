package io.seata.samples.tcc.transfer.action.impl;

import org.springframework.transaction.annotation.Transactional;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.samples.tcc.transfer.action.SecondTccAction;
import io.seata.samples.tcc.transfer.dao.AccountDAO;
import io.seata.samples.tcc.transfer.domains.Account;

/**
 * 加钱参与者实现
 *
 * @author zhangsen
 */
public class SecondTccActionImpl implements SecondTccAction {

    /**
     * 加钱账户 DAP
     */
    private AccountDAO toAccountDAO;

    /**
     * 一阶段准备，转入资金 准备
     * @param businessActionContext
     * @param accountNo
     * @param amount
     * @return
     */
    @Override
    @Transactional
    public boolean prepareAdd(final BusinessActionContext businessActionContext, final String accountNo, final double amount) {
        //分布式事务ID
        final String xid = businessActionContext.getXid();
        doAction(xid, accountNo, amount);
        return true;
    }
    
    public void doAction(String xid, final String accountNo, final double amount) {
    	try {
            //校验账户
            Account account = toAccountDAO.getAccountForUpdate(accountNo);
            if(account == null){
                System.out.println("prepareAdd: 账户["+accountNo+"]不存在, txId:" + xid);
                throw new RuntimeException("账户不存在");
            }
            //待转入资金作为 不可用金额
            double freezedAmount = account.getFreezedAmount() + amount;
            account.setFreezedAmount(freezedAmount);
            toAccountDAO.updateFreezedAmount(account);
            System.out.println(String.format("prepareAdd account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
        } catch (Throwable t) {
        	throw new RuntimeException("加钱失败");
        }
    }

    /**
     * 二阶段提交
     * @param businessActionContext
     * @return
     */
    @Override
    @Transactional
    public boolean commit(BusinessActionContext businessActionContext) {
        //分布式事务ID
        final String xid = businessActionContext.getXid();
        //账户ID
        final String accountNo = String.valueOf(businessActionContext.getActionContext("accountNo"));
        //转出金额
        final double amount = Double.valueOf(String.valueOf(businessActionContext.getActionContext("amount")));
        try {
			doCommit(xid, accountNo, amount);
		} catch (Exception e) {
			throw new RuntimeException("加钱提交事务失败");
		}
        return true;

    }
    
    public void doCommit(String xid, final String accountNo, final double amount) throws Exception {
    	Account account = toAccountDAO.getAccountForUpdate(accountNo);
        //加钱
        double newAmount = account.getAmount() + amount;
        account.setAmount(newAmount);
        //冻结金额 清除
        account.setFreezedAmount(account.getFreezedAmount()  - amount);
        toAccountDAO.updateAmount(account);

        System.out.println(String.format("add account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
    }

    /**
     * 二阶段回滚
     * @param businessActionContext
     * @return
     */
    @Override
    @Transactional
    public boolean rollback(BusinessActionContext businessActionContext) {
        //分布式事务ID
        final String xid = businessActionContext.getXid();
        //账户ID
        final String accountNo = String.valueOf(businessActionContext.getActionContext("accountNo"));
//        String accountNo = "XXX";
        //转出金额
        final double amount = Double.valueOf(String.valueOf(businessActionContext.getActionContext("amount")));
        try {
			doRollback(xid, accountNo, amount);
		} catch (Exception e) {
			throw new RuntimeException("加钱回滚事务失败");
		}
        return true;
    }
    
    public void doRollback(String xid, final String accountNo, final double amount) throws Exception {
    	Account account = toAccountDAO.getAccountForUpdate(accountNo);
        if(account == null){
            //账户不存在, 无需回滚动作
        	throw new RuntimeException("账户不存在");
        }
        //冻结金额 清除
        account.setFreezedAmount(account.getFreezedAmount()  - amount);
        toAccountDAO.updateFreezedAmount(account);

        System.out.println(String.format("Undo prepareAdd account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
    }

    public void setToAccountDAO(AccountDAO toAccountDAO) {
        this.toAccountDAO = toAccountDAO;
    }
}
