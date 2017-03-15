/*
 portfolio Logic
 */

RoboAdviceApp.service("portfolioService", function(portfolioREST, CONFIG, strategyREST, strategyService, $log){
    return{
        portfolioHistory : null,
        portfoliosRaw : {
          amounts : [],
          dates   : []
        },
        portfolioDifferences: {},

        getPortfolioHistory:   () => this.portfolioHistory,

        getPortfolioAmounts: function(){
          return this.portfoliosRaw.amounts;
        },

        getPortfolioDates: function(){
          return this.portfoliosRaw.dates;
        },

        getPortfolioDifferences: function(){
          return this.portfolioDifferences;
        },

        getFullHistory: function(usr_id, callback){
          let parent = this;
            portfolioREST.getFullHistory({user_id: usr_id}).$promise.then(function(response){
                if(response.statusCode == 0){
                  $log.debug("portfolioService.getFullHistory || Received Obj:");
                  let portfolioHistory = response.data;

                  let strategy_index = 0;

                  if(portfolioHistory != null){
                    // get strategies history
                    let strategies = strategyService.getHistory();
                    // an index for the strategies
                    $log.debug("portfolioService.setHistory| setting up amount for each strategy");
                    $log.debug("portfolioService.setHistory| strategies: " + strategies.length)

                    // for each portfolio
                    let ret = []
                    let i =0;
                    let portfolioAmount = 0;

                    if(strategies.length>0)
                      strategies[0].setInitialAmount(CONFIG["INITIAL_AMOUNT"]);

                    portfolioHistory.forEach(function(aPortfolio){
                      i++;
                      let portfolioObj = new Portfolio(aPortfolio);
                      let portfolioDate = portfolioObj.getDate();
                      portfolioAmount = portfolioObj.getTotalAmount();
                      parent.portfoliosRaw.amounts.push(portfolioAmount);
                      parent.portfoliosRaw.dates.push(portfolioObj.getDate().getTime());
                      if(strategy_index<strategies.length){
                        // attachPortfolio
                        strategies[strategy_index].attachPortfolio(portfolioObj);

                        // if the next strategy exists and has a date after the portfolio Date
                        if(strategy_index + 1 < strategies.length && strategies[strategy_index+1].getDate() <= portfolioDate){
                          // the next strategy
                          $log.debug(portfolioDate)
                          $log.debug("portfolioService.getFullHistory| next Strategy: "+strategies[strategy_index+1].getName() + " date: " + strategies[strategy_index+1].getDate());
                          strategies[strategy_index+1].setInitialAmount(portfolioAmount);
                          strategies[strategy_index].setFinalAmount(portfolioAmount);
                          strategy_index++;
                        }
                      }
                      ret.push(portfolioObj);
                    });// end foreach portfolios

                    // set parent portfolioHistory
                    parent.portfolioHistory = ret;

                    // if strategy_index < strategies number then there are some pending strategies
                    // and the strategy at index strategy_index is the last strategy with some portfolio!
                    if(strategy_index<strategies.length)
                      strategyService.setLastStrategy(strategies[strategy_index]);
                    strategy_index++;

                    while(strategy_index < strategies.length){
                      // the last amount is inside portfolioAmount
                      strategies[strategy_index].setInitialAmount(portfolioAmount);
                      strategies[strategy_index].setFinalAmount(portfolioAmount);
                      strategy_index++;
                    }

                    if(i>1){
                      $log.debug("portfolioService.setHistory| setting up portoflio Differences: ");
                      let last_portfolio_index = i-1;
                      parent.portfolioDifferences = new Array(4);
                      parent.portfolioDifferences[0]=portfolioHistory[last_portfolio_index].bondsAmount!=0 ? (
                        (portfolioHistory[last_portfolio_index].bondsAmount - portfolioHistory[last_portfolio_index-1].bondsAmount)
                        / portfolioHistory[last_portfolio_index].bondsAmount) * 100 : 0;
                      parent.portfolioDifferences[1]=portfolioHistory[last_portfolio_index].commoditiesAmount!=0 ? (
                        (portfolioHistory[last_portfolio_index].commoditiesAmount - portfolioHistory[last_portfolio_index-1].commoditiesAmount)
                        / portfolioHistory[last_portfolio_index].commoditiesAmount) * 100 : 0;
                      parent.portfolioDifferences[2]=portfolioHistory[last_portfolio_index].forexAmount!=0 ? (
                        (portfolioHistory[last_portfolio_index].forexAmount - portfolioHistory[last_portfolio_index-1].forexAmount)
                        / portfolioHistory[last_portfolio_index].forexAmount ) * 100 : 0;
                      parent.portfolioDifferences[3]=portfolioHistory[last_portfolio_index].stocksAmount!=0 ? (
                        (portfolioHistory[last_portfolio_index].stocksAmount - portfolioHistory[last_portfolio_index-1].stocksAmount)
                        / portfolioHistory[last_portfolio_index].stocksAmount ) * 100 : 0;
                    }
                    //parent.currentPortfolio = portfolioHistory[i-1];
                    //parent.portfolioList = portfolioHistory;
                    $log.debug("userService.setCurrentPortfolio| History Portfolios:");

                    callback(ret);
                  }else{
                    // portfolioHistory is empty
                    callback(null);
                  }
                }else{
                  // statusCode error
                }
            });
        },
        setPortfolioByDate: function(){}
    }
});
