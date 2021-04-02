package com.samyotech.petstand.models;

import java.io.Serializable;

public class OrderStatusDTO implements Serializable {

    String id = "";
    String order_id = "";
    String od_confirm_status = "";
    String od_confirm_date = "";
    String od_packed_status = "";
    String od_packed_date = "";
    String od_dispatched_status = "";
    String od_dispatched_date = "";
    String od_delivered_status = "";
    String od_delivered_date = "";
    String od_status = "";
    String created_at = "";
    String od_pending_date = "";
    String od_pending_status = "";

    public String getOd_pending_date() {
        return od_pending_date;
    }

    public void setOd_pending_date(String od_pending_date) {
        this.od_pending_date = od_pending_date;
    }

    public String getOd_pending_status() {
        return od_pending_status;
    }

    public void setOd_pending_status(String od_pending_status) {
        this.od_pending_status = od_pending_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOd_confirm_status() {
        return od_confirm_status;
    }

    public void setOd_confirm_status(String od_confirm_status) {
        this.od_confirm_status = od_confirm_status;
    }

    public String getOd_confirm_date() {
        return od_confirm_date;
    }

    public void setOd_confirm_date(String od_confirm_date) {
        this.od_confirm_date = od_confirm_date;
    }

    public String getOd_packed_status() {
        return od_packed_status;
    }

    public void setOd_packed_status(String od_packed_status) {
        this.od_packed_status = od_packed_status;
    }

    public String getOd_packed_date() {
        return od_packed_date;
    }

    public void setOd_packed_date(String od_packed_date) {
        this.od_packed_date = od_packed_date;
    }

    public String getOd_dispatched_status() {
        return od_dispatched_status;
    }

    public void setOd_dispatched_status(String od_dispatched_status) {
        this.od_dispatched_status = od_dispatched_status;
    }

    public String getOd_dispatched_date() {
        return od_dispatched_date;
    }

    public void setOd_dispatched_date(String od_dispatched_date) {
        this.od_dispatched_date = od_dispatched_date;
    }

    public String getOd_delivered_status() {
        return od_delivered_status;
    }

    public void setOd_delivered_status(String od_delivered_status) {
        this.od_delivered_status = od_delivered_status;
    }

    public String getOd_delivered_date() {
        return od_delivered_date;
    }

    public void setOd_delivered_date(String od_delivered_date) {
        this.od_delivered_date = od_delivered_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getOd_status() {
        return od_status;
    }

    public void setOd_status(String od_status) {
        this.od_status = od_status;
    }
}
