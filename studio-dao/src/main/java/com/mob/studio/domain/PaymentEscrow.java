package com.mob.studio.domain;

/**
 * @author: Zhang.Min
 * @since: 2016/4/15
 * @version: 1.7
 */
public class PaymentEscrow {
    private Long paymentId;
    private Long rowId;
    private Long paybackedRowId;
    private PaymentStatus status;
    private Integer isPaybacked;
    private Long price;
    private String comment;
    private Long createdOn;
    private Long updatedOn;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public Long getPaybackedRowId() {
        return paybackedRowId;
    }

    public void setPaybackedRowId(Long paybackedRowId) {
        this.paybackedRowId = paybackedRowId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Integer getIsPaybacked() {
        return isPaybacked;
    }

    public void setIsPaybacked(Integer isPaybacked) {
        this.isPaybacked = isPaybacked;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }
}
