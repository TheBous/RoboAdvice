RoboAdviceApp.component("realtimeLineGraph",{
    bindings: {
        horizzontalAxis: "<", // an array of dates in timestamp
        verticalAxis: "<",    // an array of amounts
        incrementData: "&",   // the method that increments the data
        realtime: "@",        // [true|false]
        forecastValue: "@",   // forecast value to obtain
        stimatedAmount: "<"   // the stimated amount from the controller
    },
    templateUrl: "../../html/realtimeGraph.html",
    controller: function($scope,$interval,$log){
        var $ctrl = this;
        var scope = $scope;

        this.$onInit = function(){
            let now = new Date();
            $log.debug("realtimeLineGrap| initialized")
            $log.debug(this.verticalAxis);
            $log.debug(this.horizzontalAxis);

            $scope.options = {
              scales: {
                yAxes: [{
                  ticks: {
                    beginAtZero:false
                  }
                }]
              }
            };

            $scope.getNewData = function(){
              $ctrl.currentTime = now.getDateFormatted() + " " + now.getHours() + ":" + now.getMinutes();
              //let $this = this;
              //$ctrl.incrementData({data:$ctrl.forecastValue})
            };

            if(this.realtime == "true"){
              $log.debug("realtime component| realtime is setted");
              $ctrl.interval = 5000; // 1 second
            }

            // realtime highchart
            Highcharts.chart('rt', {
                chart: {
                    type: 'spline',
                    animation: Highcharts.svg, // don't animate in old IE
                    marginRight: 10,
                    events: {
                        load: function () {
                          // set up the updating of the chart each second
                          var series = this.series[0];

                          setInterval(function () {
                            //console.log(new Date($ctrl.horizzontalAxis[$ctrl.horizzontalAxis.length-1]))

                            let rnd = Math.random()*10;
                            let salt = (Math.floor(rnd)%2) ? 1 : -1;

                            let x = $ctrl.horizzontalAxis[$ctrl.horizzontalAxis.length-1]+$ctrl.interval, // current time
                            y = $ctrl.verticalAxis[$ctrl.verticalAxis.length-1]+rnd*salt;

                            $ctrl.horizzontalAxis.push(x);
                            $ctrl.verticalAxis.push(y);
                            series.addPoint([x, y], true, true);
                          }, $ctrl.interval);

                        }
                    }
                },
                title: {
                    text: ''
                },
                xAxis: {
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
                        return '<b>' + this.series.name + '</b><br/>' +
                            Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                            Highcharts.numberFormat(this.y, 2);
                    }
                },
                legend: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                },
                series: [{
                    name: 'Our data',
                    data: (function () {
                        // set the first data
                        var data = [];
                        let portfolioNum = $ctrl.verticalAxis.length;
                        let lastValue = $ctrl.verticalAxis[portfolioNum-1];
                        var time = (new Date()).getTime();

                        for(let i = 0;i<portfolioNum; i++){
                          $ctrl.horizzontalAxis[i]=time+i*$ctrl.interval
                          data.push({
                            x: time+i*$ctrl.interval,
                            y: $ctrl.verticalAxis[i]
                          });
                        }

                        return data;

                    }())
                }]
            });

        }// end onInit

    }
});
