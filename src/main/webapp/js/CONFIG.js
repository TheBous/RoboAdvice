RoboAdviceApp.constant("CONFIG", {
    "INITIAL_AMOUNT": 10000,
    "ASSETS_NAME": ["Bonds","Stocks","Forex", "Commodities"]
});


RoboAdviceApp.config(['ChartJsProvider', function (ChartJsProvider) {
    // Configure all charts
    ChartJsProvider.setOptions({
      // bond, commodities, forex, stocks
      chartColors: ['#97BACD', '#DCDCDC','#F64649','#47BFBD'],
      responsive: true
    });
}])
