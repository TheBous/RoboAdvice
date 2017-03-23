RoboAdviceApp.service("portfolioREST",
    function($resource,$cookies,TokenHandler){
        var baseUrl = "http://localhost:8080/portfolio/";
        return $resource(baseUrl, {}, {
            getCurrent: {
                method: 'POST',
                url: baseUrl + 'getCurrent',
                params: {
                    user_id: '@user_id'
                }
            },//end getCurrent

            getFullHistory: {
                method: 'POST',
                url: baseUrl + 'getFullHistory',
                params: {
                    user_id: '@user_id'
                }
            },

            getPortfolioByDate: {
                method: 'POST',
                url: baseUrl + 'getPortfoliobyDate',
                params: {
                    user_id: '@user_id'
                }
            },
            backtesting: {
                method: 'POST',
                url: baseUrl + 'backtesting',
                params: {
                    fromDate: '@fromDate'
                }
            },

            advice: {
                method: 'POST',
                url: baseUrl + 'advice',
                params: {
                    strategy: '@strategy'
                }
            },

            getAssetsTrend: {
                method: 'POST',
                url: "/assetsclass/trend"
            }
        })
    });
