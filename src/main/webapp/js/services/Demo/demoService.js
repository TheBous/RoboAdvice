RoboAdviceApp.service("demoService", function(demoREST, CONFIG, $log){
    return {
        forecastDemo: {},
        getDemoForecasting(callback){
            let parent = this;
            $log.debug("PortfolioService | BackTesting call");
            demoREST.forecast().$promise.then(function(response){
                if(response.statusCode == 0) {
                    $log.debug("demoService | statusCode = 0");
                    let forecastDemo = response.data;
                    let ret = [];
                    forecastDemo.forEach(function(element){
                        let portfolioObj = new Portfolio(element);
                        ret.push(portfolioObj);
                    });
                    parent.forecastDemo = ret;
                    if(callback)
                        callback(response);
                    $log.debug("forecast Demo:" + parent.forecastDemo);
                }
                else{
                    $log.debug("demoService | statusCode = 1");
                    if(callback)
                        callback(response);
                }
            });

        },
        backtesting: {
            bonds : (fromDate) =>
                demoREST.backtesting({fromDate},{bonds_p:'95', stocks_p:'0', forex_p:'0', commodities_p:'5', name:'Bonds'}),

            income : (fromDate) =>
                demoREST.backtesting({fromDate},{bonds_p:'65', stocks_p:'10', forex_p:'15', commodities_p:'10', name:'Income'}),

            balanced : (fromDate) =>
                demoREST.backtesting({fromDate},{bonds_p:'30', stocks_p:'30', forex_p:'20', commodities_p:'20', name:'Balanced'}),

            growth : (fromDate) =>
                demoREST.backtesting({fromDate},{bonds_p:'20', stocks_p:'60', forex_p:'10', commodities_p:'10', name:'Growth'}),

            stocks : (fromDate) =>
                demoREST.backtesting({fromDate},{bonds_p:'0', stocks_p:'100', forex_p:'0', commodities_p:'0', name:'Stocks'}),

            custom : (strategy_name,percentages,fromDate) =>
                demoREST.backtesting({fromDate},{bonds_p: ''+percentages[0], stocks_p:''+percentages[1], forex_p:''+percentages[2], commodities_p:''+percentages[3], name:strategy_name})
        },
    }
});
