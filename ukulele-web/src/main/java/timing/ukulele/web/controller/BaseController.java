package timing.ukulele.web.controller;

import timing.ukulele.common.data.ResponseCode;
import timing.ukulele.common.data.ResponseData;

/**
 * @author fengxici
 */
public abstract class BaseController {

    protected <T> ResponseData<T> successResponse(T data, String msg) {
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    protected <T> ResponseData<T> successResponse(T data) {
        return new ResponseData<>(ResponseCode.SUCCESS, data);
    }

    protected <T> ResponseData<T> successResponse() {
        return new ResponseData<>(ResponseCode.SUCCESS);
    }

    protected <T> ResponseData<T> failResponse(T data, String msg) {
        return new ResponseData<>(ResponseCode.BUSINESS_ERROR.getCode(), msg, data);
    }

    protected <T> ResponseData<T> failResponse(T data) {
        return new ResponseData<>(ResponseCode.BUSINESS_ERROR, data);
    }

    protected <T> ResponseData<T> failResponse() {
        return new ResponseData<>(ResponseCode.BUSINESS_ERROR);
    }

    protected <T> ResponseData<T> errorResponse(T data, String msg) {
        return new ResponseData<>(ResponseCode.ERROR.getCode(), msg, data);
    }

    protected <T> ResponseData<T> errorResponse(T data) {
        return new ResponseData<>(ResponseCode.ERROR, data);
    }

    protected <T> ResponseData<T> errorResponse() {
        return new ResponseData<>(ResponseCode.ERROR);
    }

    protected <T> ResponseData<T> paraErrorResponse(T data, String msg) {
        return new ResponseData<>(ResponseCode.PARA_ERROR.getCode(), msg, data);
    }

    protected <T> ResponseData<T> paraErrorResponse(T data) {
        return new ResponseData<>(ResponseCode.PARA_ERROR, data);
    }

    protected <T> ResponseData<T> paraErrorResponse() {
        return new ResponseData<>(ResponseCode.PARA_ERROR);
    }

}
