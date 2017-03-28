RoboAdviceApp.service("demoREST",
    function($resource){
        var baseUrl = "/demo/";
        return $resource(baseUrl, {}, {
            forecast: {
                method: 'POST',
                url: baseUrl + 'forecast',
            },
            backtesting: {
                method: 'POST',
                url: baseUrl + 'backtesting',
                headers: {'Content-Type': 'application/json; charset=UTF-8' },
                params: {
                    fromDate: '@fromDate'
                }
            },
        })
    });

