//var priceReg = /(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/;
var priceReg = /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/

function priceCheck(val) {
    if (!priceReg.test(val)){
        return false;
    }
    return true;
}