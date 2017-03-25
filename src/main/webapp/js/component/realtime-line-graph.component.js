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
        var $ctrl = this;

        this.$onInit = function(){
            let rtid="rt" + Math.floor(1+Math.random()*1000);
            $(".realtime_wrapper > div").attr("id",rtid);
            $scope.rtid=rtid;
            let now = new Date();
            $log.debug("realtimeLineGrap| initialized")
            //this.incrementData();
            $scope.options = {
              scales: {
                yAxes: [{
                  ticks: {
                    beginAtZero:false
                  }
                }]
              }
            };
            $ctrl.interval = 5000;

            this.getNewData = function(){

              let rnd = Math.random()*10;
              let salt = (Math.floor(rnd)%2) ? 1 : -1;
              let series = $ctrl.series;
              let x = $ctrl.horizzontalAxis[$ctrl.horizzontalAxis.length-1]+$ctrl.interval, // current time
              y = $ctrl.verticalAxis[$ctrl.verticalAxis.length-1]+rnd*salt;
              //$ctrl.incrementData();
              $ctrl.horizzontalAxis.push(x);
              $ctrl.verticalAxis.push(y);
              $ctrl.incrementData({data:[$ctrl.horizzontalAxis,$ctrl.verticalAxis]});
              series.addPoint([x, y], true, true);
            }

            let showDates = $ctrl.showDates == "true" ? true : false;

            // realtime highchart
            Highcharts.chart(rtid, {
                chart: {
                    type: 'spline',
                    animation: Highcharts.svg, // don't animate in old IE
                    marginRight: 10,
                    events: {
                        load: function () {
                          // set up the updating of the chart each second
                          $ctrl.series = this.series[0];
                          if($ctrl.realtime == "true"){
                            setInterval($ctrl.getNewData, $ctrl.interval);
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
                        return '<b>' + this.series.name + '</b><br/>' +
                            //Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
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
                    name: 'Portfolio Amount',
                    data: (function () {
                        // set the first data
                        var data = [];
                        let portfolioNum = $ctrl.verticalAxis.length;
                        let lastValue = $ctrl.verticalAxis[portfolioNum-1];
                        var time = (new Date()).getTime();

                        for(let i = 0;i<portfolioNum; i++){

                          if($ctrl.realtime == "true"){
                            timeAxis = time+i*$ctrl.interval;
                          }else{
                            timeAxis = $ctrl.horizzontalAxis[i];
                          }

                          data.push({
                            x: timeAxis,
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
