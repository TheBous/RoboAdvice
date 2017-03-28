RoboAdviceApp.component("realtimeLineGraph",{
    bindings: {
        horizzontalAxis: "<", // an array of dates in timestamp
        verticalAxis: "<",    // an array of amounts
        realtime: "@",        // [true|false]
        forecastValue: "@",   // forecast value to obtain
        incrementData: "&",   // method to increment and save the state of the graph
        stimatedAmount: "<",   // stimated amount, it changes
        showDates: "@"        // [true|false]
    },
    templateUrl: "../../html/realtimeGraph.html",
    controller: function($scope,$interval,$log){
        var rtid="rt" + Math.floor(1+Math.random()*1000);
        $(".realtime_wrapper > div").attr("id",rtid);
        $scope.rtid=rtid;

        var $ctrl = this;

        this.$onChanges = function(obj){
            $log.error(obj);
            $ctrl.horizzontalAxis = this.horizzontalAxis;
            $ctrl.verticalAxis = this.verticalAxis;
            this.setGraph();
        }

        this.$onInit = function(){
            let now = new Date();
            $log.debug("realtimeLineGrap| initialized")
            $log.debug("verticalAxis:");
            $log.debug($ctrl.verticalAxis);
            $log.debug("horizzontalAxis:");
            $log.debug($ctrl.horizzontalAxis);
            //this.incrementData();
            let showDates = $ctrl.showDates == "true" ? true : false;
        }// end onInit

        this.setGraph = function(){
            // realtime highchart
            $ctrl.interval = 1000;

            Highcharts.chart($scope.rtid, {
                chart: {
                    type: 'spline',
                    animation: Highcharts.svg, // don't animate in old IE
                    marginRight: 10,
                    events: {
                        load: function () {
                            // set up the updating of the chart each second
                            $ctrl.series = this.series[0];
                            var series = this.series[0];
                            let iteration = 1;
                            if($ctrl.realtime == "true"){
                                setInterval(function () {
                                    let salt = Math.random()*0.5;
                                    if(iteration<99)
                                        iteration++;
                                    else
                                        iteration = 1;

                                    let series = $ctrl.series;
                                    let height = (

                                            $ctrl.stimatedAmount - ($ctrl.verticalAxis[$ctrl.verticalAxis.length-1])
                                        )/(100-iteration);
                                    let rnd = Math.random()*(height);

                                    if(iteration%2 == 0)
                                        y = $ctrl.verticalAxis[$ctrl.verticalAxis.length-1]+rnd;
                                    else y = $ctrl.verticalAxis[$ctrl.verticalAxis.length-1]-rnd/2;


                                    let x = $ctrl.horizzontalAxis[$ctrl.horizzontalAxis.length-1]+$ctrl.interval;
                                    $ctrl.horizzontalAxis.push(x);
                                    $ctrl.verticalAxis.push(y);



                                    series.addPoint([x, y], true, true);
                                }, $ctrl.interval);
                            }
                        }
                    }
                },
                title: {
                    text: ''
                },
                xAxis: {
                    labels: {
                        enabled: $ctrl.showDates == "true" ? true : false
                    },
                    type: 'datetime',
                    tickPixelInterval: 150
                },
                yAxis: {
                    title: {
                        text: 'Value'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                },
                tooltip: {
                    formatter: function () {
                        let ret = '<b>' + this.series.name + '</b><br/>';
                        if($ctrl.showDates == "true")
                            ret = ret + Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>';
                        ret = ret + Highcharts.numberFormat(this.y, 2);
                        return ret;
                    }
                },
                legend: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                },
                series: [{
                    name: 'Portfolio Amount',
                    data: (function () {
                        // set the first data
                        var data = [];
                        let portfolioNum = $ctrl.verticalAxis.length;
                        let lastValue = $ctrl.verticalAxis[portfolioNum-1];
                        var time = (new Date()).getTime();


                        for(let i = 0;i<portfolioNum; i++){
                            timeAxis = $ctrl.horizzontalAxis[i];

                            data.push({
                                x: timeAxis,
                                y: $ctrl.verticalAxis[i]
                            });
                        }

                        return data;

                    }())
                }]
            });
        }

    }
});
