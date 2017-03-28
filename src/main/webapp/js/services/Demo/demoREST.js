RoboAdviceApp.service("demoREST",
    function($resource,$cookies,TokenHandler){
        var baseUrl = "/demo/";
        return $resource(baseUrl, {}, {
            getPortfolioByDate: {
                method: 'POST',
                url: baseUrl + 'forecast',
            },
        })
    });

