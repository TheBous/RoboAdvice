/*
 portfolio Logic
 */

RoboAdviceApp.service("portfolioService", function(portfolioREST, CONFIG, strategyREST, strategyService, $log){
    return{
        adviceAmount : [],
        backtestingDatas: {},
        portfolioHistory : null,
        portfoliosRaw : {
            amounts : [],
            dates   : [],
            class_assets : [
                [],
                [],
                [],
                []
            ],

        },
        portfolioDifferences: {},

        getPortfolioHistory()  {return this.portfolioHistory},

        getPortfolioAmounts(){
            return this.portfoliosRaw.amounts;
        },

        getClassAssets(){
            return this.portfoliosRaw.class_assets;
        },
        getPortfolioDates(){
            return this.portfoliosRaw.dates;
        },

        getPortfolioDifferences(){
            return this.portfolioDifferences;
        },

        getFullHistory(callback){
            let parent = this;
            portfolioREST.getFullHistory().$promise.then(function(response){
                if(response.statusCode == 0){
                    $log.debug("portfolioService.getFullHistory");
                    let portfolioHistory = response.data;

                    let strategy_index = 0;

                    if(portfolioHistory != null){
                        // get strategies history
                        let strategies = strategyService.getHistory();
                        // an index for the strategies
                        $log.debug("portfolioService.setHistory| setting up amount for each strategy");
                        $log.debug("portfolioService.setHistory| strategies: " + strategies.length)

                        // for each portfolio
                        let ret = [];
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
                            parent.portfoliosRaw.dates.push((portfolioObj.getDate().getTime())+(3600*24*1000));
                            parent.portfoliosRaw.class_assets[0].push(portfolioObj.getBondsAmount());
                            parent.portfoliosRaw.class_assets[1].push(portfolioObj.getStocksAmount());
                            parent.portfoliosRaw.class_assets[2].push(portfolioObj.getForexAmount());
                            parent.portfoliosRaw.class_assets[3].push(portfolioObj.getCommoditiesAmount());

                            if(strategy_index<strategies.length){
                                // attachPortfolio
                                strategies[strategy_index].attachPortfolio(portfolioObj);

                                // if the next strategy exists and has a date after the portfolio Date
                                if(strategy_index + 1 < strategies.length && strategies[strategy_index+1].getDate() <= portfolioDate){
                                    // the next strategy
                                    $log.debug("portfolioService.getFullHistory| next Strategy name: "+strategies[strategy_index+1].getName());
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
                            $log.debug("portfolioService.setHistory| setting up portfolio Differences: ");
                            let last_portfolio_index = i-1;
                            parent.portfolioDifferences = new Array(4);

                            parent.portfolioDifferences[0]={
                                percentage: portfolioHistory[last_portfolio_index].bondsAmount!=0 ? (
                                (portfolioHistory[last_portfolio_index].bondsAmount - portfolioHistory[last_portfolio_index-1].bondsAmount)
                                / portfolioHistory[last_portfolio_index].bondsAmount) * 100 : 0,
                                amount: portfolioHistory[last_portfolio_index].bondsAmount
                            }
                            parent.portfolioDifferences[1]={
                              percentage: portfolioHistory[last_portfolio_index].stocksAmount!=0 ? (
                                (portfolioHistory[last_portfolio_index].stocksAmount - portfolioHistory[last_portfolio_index-1].stocksAmount)
                                / portfolioHistory[last_portfolio_index].stocksAmount ) * 100 : 0,
                                amount: portfolioHistory[last_portfolio_index].stocksAmount
                              }
                            parent.portfolioDifferences[2]={
                                percentage: portfolioHistory[last_portfolio_index].forexAmount!=0 ? (
                                (portfolioHistory[last_portfolio_index].forexAmount - portfolioHistory[last_portfolio_index-1].forexAmount)
                                / portfolioHistory[last_portfolio_index].forexAmount ) * 100 : 0,
                                amount: portfolioHistory[last_portfolio_index].forexAmount
                            }
                            parent.portfolioDifferences[3]={
                              percentage: portfolioHistory[last_portfolio_index].commoditiesAmount!=0 ? (
                                (portfolioHistory[last_portfolio_index].commoditiesAmount - portfolioHistory[last_portfolio_index-1].commoditiesAmount)
                                / portfolioHistory[last_portfolio_index].commoditiesAmount) * 100 : 0,
                                amount: portfolioHistory[last_portfolio_index].commoditiesAmount
                            }
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
        //unused function for future
        setPortfolioByDate(){},

        getBacktesting(param, callback){
            let parent = this;
            $log.debug("PortfolioService | BackTesting call");
            portfolioREST.backtesting({fromDate: param}).$promise.then(function(response) {
                if(response.statusCode == 0){
                    $log.debug("PortfolioService | statusCode = 0");

                    $log.debug(response.data);
                    let backtestingDatas = response.data;
                    let ret = [];
                    backtestingDatas.forEach(function(element){
                        let portfolioObj = new Portfolio(element);
                        ret.push(portfolioObj);
                    });
                    parent.backtestingDatas = ret;
                    if(callback)
                        callback(response);
                    $log.debug("backtestingDatas:" + parent.backtestingDatas);
                }
                else{
                    $log.debug("backtesting| error on  backtesting");
                    if(callback)
                        callback(response);
                }
            });
        },
        getForecasting(callback){
          let parent = this;
          $log.debug("PortfolioService.forecasting called");
          portfolioREST.forecasting().$promise.then(function(response){
              if(response.statusCode == 0){
                $log.debug("PortfolioService.forecasting response 0")
                let ret = {
                  amounts : new Array(),
                  dates : new Array()
                };
                parent.forecast = new Array();
                response.data.forEach(function(element){
                    let portfolioObj = new Portfolio(element);
                    parent.forecast.push(portfolioObj);
                    ret.amounts.push(portfolioObj.getTotalAmount());
                    ret.dates.push(portfolioObj.getDate());
                });
                callback(ret);
              }else{
                $log.error("PortfolioService.forecasting response error");
                callback(false)
              }
          });
        }
    }
});
