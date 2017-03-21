var Strategy = class {

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
     this.portfoliosHistory = [];
     this.assetsData = [this.bonds_p,this.stocks_p,this.forex_p,this.commodities_p];
     this.assetsLabel = ["Bonds","Stocks","Forex","Commodities"];
     this.options= {legend: {display: true}};

     //date
     this.today = new Date();
     this.todayFormatted = {
       day : this.today.getDayFormatted(),
       month : this.today.getMonthFormatted(),
       year : this.today.getUTCFullYear()
     };
     // the strategy formatted date
     this.dateFormatted = {
       day : this.date.getDayFormatted(),
       month : this.date.getMonthFormatted(),
       year : this.date.getUTCFullYear()
     }

   } // end constructor

   attachPortfolio(portfolio){
     this.portfolios.amounts.push(portfolio.getTotalAmount());
     this.portfolios.dates.push(portfolio.getDate());
     this.portfoliosHistory.push(portfolio);
     // set the final amount
     this.final_amount = portfolio.getTotalAmount();
   }
   isPending(){
     return (this.dateFormatted.day == this.todayFormatted.day &&
     this.dateFormatted.month == this.todayFormatted.month &&
     this.dateFormatted.year == this.todayFormatted.year &&
     this.isActive)
   }

   setInitialAmount(initialAmount){
     this.initial_amount = initialAmount;
   }

   setFinalAmount(finalAmount){
     this.final_amount = finalAmount;
   }

   getInitialAmount(){          return this.initial_amount; }
   getFinalAmount(){            return this.final_amount; }
   getHistoryAmount(){          return this.portfolios.amounts; }
   getHistoryDate(){            return this.portfolios.dates; }
   isCurrent(){                 return false; }
   getAssets(){                 return this.assetsData; }
   getFormattedDate(){          return this.dateFormatted.day + "/" + this.dateFormatted.month + "/" + this.dateFormatted.year; }
   getDate(){                   return this.date; }
   getName(){                   return this.name; }
   getPortfolios(){             return this.portfoliosHistory; }
 };
