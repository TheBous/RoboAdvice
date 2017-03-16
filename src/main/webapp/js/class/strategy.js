/*class Strategy(strategyObj){
    constructor(strategyObj){
        this.name = strategyObj.name;
        this.date = new Date(strategyObj.date.year+"/"+strategyObj.date.monthValue+"/"+strategyObj.date.dayOfMonth);
        this.isActive = strategyObj.active ? true : false;
        this.bonds_p = strategyObj.bonds_p;
        this.stocks_p = strategyObj.stocks_p;
        this.forex_p = strategyObj.forex_p;
        this.commodities_p = strategyObj.commodities_p;

        this.initial_amount = 0;
        this.final_amount = 0;

        // should be an array of type Portfolio
        this.portfoliosAmount = [];
        this.portfolios = {
            amounts: [],
            dates: []
        };
        this.assetsData = [this.bonds_p,this.stocks_p,this.forex_p,this.commodities_p];
        this.assetsLabel = ["Bonds","Stocks","Forex","Commodities"];
        this.options= {legend: {display: true}};
    }
    getInitialAmount = () => this.initial_amount;
    getFinalAmount = () => this.final_amount;
    setInitialAmount = (initialAmount) => this.initial_amount = initialAmount;
    setFinalAmount = (finalAmount) => this.final_amount = finalAmount;

    //date
    var today = new Date();
    this.todayFormatted = {
        day : today.getDayFormatted(),
        month: today.getMonthFormatted(),
        year: today.getUTCFullYear()
    }
    // the strategy formatted date
    this.dateFormatted = {
        day : this.date.getDayFormatted(),
        month : this.date.getMonthFormatted(),
        year : this.date.getUTCFullYear()
    }
    getHistoryAmount = () => this.portfolios.amounts;
    getHistoryDate = () => this.portfolios.dates;
    attachPortfolio = (portfolio) => {
        this.portfolios.amounts.push(portfolio.getTotalAmount());
        this.portfolios.dates.push(portfolio.getFormattedDate());
        this.final_amount = portfolio.getTotalAmount();
    }
    isPending = () => {
        this.dateFormatted.day == this.todayFormatted.day &&
        this.dateFormatted.month == this.todayFormatted.month &&
        this.dateFormatted.year == this.todayFormatted.year &&
        this.isActive
    }

    isCurrent = () => false;

    getAssets = () => this.assetsData;
    getFormattedDate = () => this.dateFormatted.day + "/" + this.dateFormatted.month + "/" + this.dateFormatted.year;
    getDate = () => this.date;
    getName = () => this.name;

}*/

var Strategy = function(strategyObj){
    this.obj = strategyObj;
    this.name = strategyObj.name;
    this.date = new Date(strategyObj.date.year+"/"+strategyObj.date.monthValue+"/"+strategyObj.date.dayOfMonth);
    this.isActive = strategyObj.active ? true : false;
    this.bonds_p = strategyObj.bonds_p;
    this.stocks_p = strategyObj.stocks_p;
    this.forex_p = strategyObj.forex_p;
    this.commodities_p = strategyObj.commodities_p;

    this.initial_amount = 0;
    this.final_amount = 0;

    // should be an array of type Portfolio
    this.portfoliosAmount = [];
    this.portfolios = {
        amounts: [],
        dates: []
    };

    // the initial amount, if the strategy is the first strategy, the amount is equal to 10000
    //this.getInitialAmount = () => this.portfolios[0].getAmount();
    this.getInitialAmount = () => {return this.initial_amount;}


    // if the strategy is the actual strategy, finalAmount is equal to the currentAmount
    //this.getFinalAmount = () => this.portfolios[this.portfolios.length-1].getAmount()

    this.getFinalAmount = () => this.final_amount;

    this.setInitialAmount = function(initialAmount){
        this.initial_amount = initialAmount;
    }
    this.setFinalAmount = function(finalAmount){
        this.final_amount = finalAmount;
    }

    var today = new Date();
    this.todayFormatted = {
        day : today.getDayFormatted(),
        month: today.getMonthFormatted(),
        year: today.getUTCFullYear()
    }
    // the strategy formatted date
    this.dateFormatted = {
        day : this.date.getDayFormatted(),
        month : this.date.getMonthFormatted(),
        year : this.date.getUTCFullYear()
    }

    // strategy Assets
    this.assetsData = [this.bonds_p,this.stocks_p,this.forex_p,this.commodities_p];
    this.assetsLabel = ["Bonds","Stocks","Forex","Commodities"];
    this.options= {legend: {display: true}};

    this.getHistoryAmount = function(){
        return this.portfolios.amounts;
    }

    this.getHistoryDate = function(){
        return this.portfolios.dates;
    }

    this.attachPortfolio = function(portfolio){
        //console.log("strategy.attachPortfolio| new portfolio, amount:",portfolio.getTotalAmount()," date: ",portfolio.getDate(),"at the strategy " + this.getName());
        // create the strategy portfolios
        this.portfolios.amounts.push(portfolio.getTotalAmount());
        this.portfolios.dates.push(portfolio.getFormattedDate());

        // set the final amount
        this.final_amount = portfolio.getTotalAmount();

        //this.portfoliosAmount.push(portfolio.getTotalAmount());
        //this.portfolios.portfolioDate.push(portfolio.getDate());
    }

    this.isPending = function(){
        // if the strategy's day is today then it is in pending
        return(
            this.dateFormatted.day == this.todayFormatted.day &&
            this.dateFormatted.month == this.todayFormatted.month &&
            this.dateFormatted.year == this.todayFormatted.year &&
            this.isActive
        )
    };

    this.isCurrent = function(){
        // TODO, how to do this method without a complex algorithm
        return false;
    }

    this.getAssets = () => this.assetsData;
    this.getFormattedDate = ()   => this.dateFormatted.day + "/" + this.dateFormatted.month + "/" + this.dateFormatted.year;
    this.getDate = ()            => this.date;
    this.getName = ()   => this.name;
}
