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
                    $log.debug("forecast Demo:" + parent.forecastDemo);                }
                else{
                    $log.debug("demoService | statusCode = 1");
                    if(callback)
                        callback(response);
                }
            });

        },
    }
});