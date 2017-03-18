function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

Date.prototype.getMonthFormatted = function() {
var month = this.getMonth() + 1;
return month < 10 ? '0' + month : month;
}

Date.prototype.getDayFormatted = function() {
var date = this.getDate();
return date < 10 ? '0' + date : date;
}

Date.prototype.getDateFormatted = function(){
  return this.getUTCFullYear()+"/"+this.getMonthFormatted()+"/"+this.getDayFormatted();
}
