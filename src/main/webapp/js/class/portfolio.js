/*
class Portfolio {
    constructor(PortObj){
        this.totalAmount = PortObj.totalAmount;
        this.date = new Date(PortObj.date.year+"/"+PortObj.date.monthValue+"/"+PortObj.date.dayOfMonth);
        this.bondsAmount = PortObj.bondsAmount;
        this.bondsPercentage = PortObj.bondsPercentage;
        this.commoditiesAmount = PortObj.commoditiesAmount;
        this.commoditiesPercentage = PortObj.commoditiesPercentage;
        this.forexAmount = PortObj.forexAmount;
        this.forexPercentage = PortObj.forexPercentage;
        this.stocksAmount = PortObj.stocksAmount;
        this.stocksPercentage = PortObj.stocksPercentage;
    }

    getBondsAmount = () => this.bondsAmount;
    getBondsPercentage = () => this.bondsPercentage;
    getCommoditiesAmount = () => this.commoditiesAmount;
    getCommoditiesPercentage = () => this.commoditiesPercentage;
    getForexAmount = () => this.forexAmount;
    getForexPercentage = () => this.forexPercentage;
    getStocksAmount = () => this.stocksAmount;
    getStocksPercentage = () => this.stocksPercentage;
    getTotalAmount = () => this.totalAmount;
    getFormattedDate = ()   => this.dateFormatted.day + "/" + this.dateFormatted.month + "/" + this.dateFormatted.year;
    getDate = () => this.date;
}
*/




var Portfolio = function(PortObj){
 this.totalAmount = PortObj.totalAmount;
 this.date = new Date(parseInt(PortObj.date.year),parseInt(PortObj.date.monthValue)-1,parseInt(PortObj.date.dayOfMonth));
 this.bondsAmount = PortObj.bondsAmount;
 this.bondsPercentage = PortObj.bondsPercentage;
 this.commoditiesAmount = PortObj.commoditiesAmount;
 this.commoditiesPercentage = PortObj.commoditiesPercentage;
 this.forexAmount = PortObj.forexAmount;
 this.forexPercentage = PortObj.forexPercentage;
 this.stocksAmount = PortObj.stocksAmount;
 this.stocksPercentage = PortObj.stocksPercentage;

 //date
 var today = new Date();
 this.todayFormatted = {
   day : today.getDayFormatted(),
   month: today.getMonthFormatted(),
   year: today.getUTCFullYear()
 };

 // the strategy formatted date
 this.dateFormatted = {
   day : this.date.getDayFormatted(),
   month : this.date.getMonthFormatted(),
   year : this.date.getUTCFullYear()
 };

 //console.log("-----------------------------")
 //console.log(parseInt(PortObj.date.year),parseInt(PortObj.date.monthValue)-1,parseInt(PortObj.date.dayOfMonth))
 //console.log(" this.date: " + this.date)


 this.getBondsAmount = () => this.bondsAmount;
 this.getBondsPercentage = () => this.bondsPercentage;
 this.getCommoditiesAmount = () => this.commoditiesAmount;
 this.getCommoditiesPercentage = () => this.commoditiesPercentage;
 this.getForexAmount = () => this.forexAmount;
 this.getForexPercentage = () => this.forexPercentage;
 this.getStocksAmount = () => this.stocksAmount;
 this.getStocksPercentage = () => this.stocksPercentage;
 this.getTotalAmount = () => this.totalAmount;
 this.getFormattedDate = ()   => this.dateFormatted.day + "/" + this.dateFormatted.month + "/" + this.dateFormatted.year;
 this.getDate = () => this.date;
 };
