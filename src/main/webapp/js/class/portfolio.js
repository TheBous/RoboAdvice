
var Portfolio = class {
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
        this.dateFormatted = {
          day: this.date.getDayFormatted(),
          month: this.date.getMonthFormatted(),
          year: this.date.getUTCFullYear()
        }
    }

    getBondsAmount(){           return this.bondsAmount; }
    getBondsPercentage(){       return this.bondsPercentage; }
    getCommoditiesAmount(){     return this.commoditiesAmount; }
    getCommoditiesPercentage(){ return this.commoditiesPercentage; }
    getForexAmount(){           return this.forexAmount; }
    getForexPercentage(){       return this.forexPercentage; }
    getStocksAmount(){          return this.stocksAmount; }
    getStocksPercentage(){      return this.stocksPercentage; }
    getTotalAmount(){           return this.totalAmount; }
    getFormattedDate(){         return this.dateFormatted.day + "/" + this.dateFormatted.month + "/" + this.dateFormatted.year; }
    getDate(){                  return this.date; }
}
