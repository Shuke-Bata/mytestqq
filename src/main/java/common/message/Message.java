package common.message;

import common.po.Account;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title Message
 * @date 2020/6/5 16:21
 * @modified by:
 */
public class Message implements Serializable {
    private static final long serialVersionUID = -6743567631108323096L;

    private MessageType messageType;
    private Account fromAccount;
    private List<Account> toAccount;
    private String message;
    private Date date;

    public Message() {
    }

    public Message(MessageType messageType, Account fromAccount) {
        this.messageType = messageType;
        this.fromAccount = fromAccount;
    }

    public Message(MessageType messageType, Account fromAccount, List<Account> toAccount,
                   String message, Date date) {
        this.messageType = messageType;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.message = message;
        this.date = date;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public List<Account> getToAccount() {
        return toAccount;
    }

    public void setToAccount(List<Account> toAccount) {
        this.toAccount = toAccount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
