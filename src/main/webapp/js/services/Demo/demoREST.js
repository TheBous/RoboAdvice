RoboAdviceApp.service("demoREST",
    function($resource,$cookies,TokenHandler){
        var baseUrl = "/demo/";
        return $resource(baseUrl, {}, {
            forecast: {
                method: 'POST',
                url: baseUrl + 'forecast',
            },
        })
    });

