/*
 Strategy Logic
 */
RoboAdviceApp.service("strategyService",function(strategyREST, $log, CONFIG){
    return {
        // the user's strategies history
        strategyHistory: [],
        lastStrategy: {},

        // get current strategy
        getCurrent: (usr_id) => strategyREST.getCurrent({user_id: usr_id}),

        getCurrentStrategy: function(){
            //if(!this.strategyHistory[this.strategyHistory.length-1].isPending()){
            return this.strategyHistory[this.strategyHistory.length-1];
            //}
            //console.log("return the last")

            //return this.lastStrategy;
        },
        // set history and save it in the local variable
        newStrategy: function(strategyObj){
            /*
             The new strategy will be the last
             */
            if(this.strategyHistory.length == 0){
                // is the first strategy
                strategyObj.setInitialAmount(CONFIG["INITIAL_AMOUNT"]);
            }
            this.strategyHistory.push(strategyObj);
        },
        setHistory: function(usr_id,callback){
            let parent = this;
            strategyREST.getHistory({user_id: usr_id}).$promise.then(function(response){
                if(response.statusCode == 0){
                    let strategyHistory = response.data;
                    let ret = [];
                    strategyHistory.forEach(function(anElement){
                        let strategyObj = new Strategy(anElement);
                        ret.push(strategyObj);
                    });
                    parent.strategyHistory = ret;
                    if(callback)
                        callback(ret);
                }else{
                    // something is not working
                    $log.error("strategyService | error on fetching strategy history");
                    if(callback)
                        callback(ret);
                }
            });
        },
        getHistory : function(){
            return this.strategyHistory;
        },
        // setLastStrategy: function(usr_id){
        //   var parent = this;
        //   strategyREST.getLastStrategy({user_id: usr_id}).$promise.then(function(response){
        //     if(response.statusCode == 0){
        //       parent.lastStrategy = new Strategy(response.data);
        //       //callback(response.data);
        //     }else{
        //       //callback(null);
        //     }
        //   });
        // },
        setLastStrategy: function(strategyObj){
            //$log.error(strategyObj)
            this.lastStrategy = strategyObj;
        },
        getLastStrategy: function(){
            return this.lastStrategy;
        },
        // insert a new strategy for the user
        insert: {
            bonds : (usr_id) =>
                strategyREST.insert({user_id: usr_id, bonds_p:'95', stocks_p:'0', forex_p:'0', commodities_p:'5', name:'Bonds'}),

            income : (usr_id) =>
                strategyREST.insert({user_id: usr_id, bonds_p:'65', stocks_p:'10', forex_p:'15', commodities_p:'10', name:'Income'}),

            balanced : (usr_id) =>
                strategyREST.insert({user_id: usr_id, bonds_p:'30', stocks_p:'30', forex_p:'20', commodities_p:'20', name:'Balanced'}),

            growth : (usr_id) =>
                strategyREST.insert({user_id: usr_id, bonds_p:'20', stocks_p:'60', forex_p:'10', commodities_p:'10', name:'Growth'}),

            stocks : (usr_id) =>
                strategyREST.insert({user_id: usr_id, bonds_p:'0', stocks_p:'100', forex_p:'0', commodities_p:'0', name:'Stocks'}),

            custom : (usr_id,strategy_name,percentages) =>
                strategyREST.insert({user_id: usr_id, bonds_p: ''+percentages[0], stocks_p:''+percentages[1], forex_p:''+percentages[2], commodities_p:''+percentages[3], name:strategy_name})
        },
        // get standard hard-coded strategies
        getStandardStrategies : function(){
            return [
                { name: "Bonds", strategy:
                    [
                        {"name": "Bonds", "percentage": 95 },
                        {"name": "Stocks", "percentage": 0 },
                        {"name": "Forex", "percentage": 0 },
                        {"name": "Commodities", "percentage": 5 }
                    ]
                },
                { name: "Income", strategy:
                    [
                        {"name": "Bonds", "percentage": 65 },
                        {"name": "Stocks", "percentage": 10 },
                        {"name": "Forex", "percentage": 15 },
                        {"name": "Commodities", "percentage": 10 }

                    ]
                },
                { name: "Balanced", strategy:
                    [
                        {"name": "Bonds", "percentage": 30 },
                        {"name": "Stocks", "percentage": 30 },
                        {"name": "Forex", "percentage": 20 },
                        {"name": "Commodities", "percentage": 20 }

                    ]
                },
                { name: "Growth", strategy:
                    [
                        {"name": "Bonds", "percentage": 20 },
                        {"name": "Stocks", "percentage": 60 },
                        {"name": "Forex", "percentage": 10 },
                        {"name": "Commodities", "percentage": 10 }

                    ]
                },
                { name: "Stocks", strategy:
                    [
                        {"name": "Bonds", "percentage": 0 },
                        {"name": "Stocks", "percentage": 100 },
                        {"name": "Forex", "percentage": 0 },
                        {"name": "Commodities", "percentage": 0 }

                    ]
                }
            ]
        },
        // end get standard strategy
        deletePending: function(callback){
            let parent = this;
            $log.debug("strategyService.deletePending| actual length: " + parent.strategyHistory.length);
            strategyREST.deletePending().$promise.then(function (response){
                if (response.statusCode==0){
                    $log.debug("strategyService.deletePending | statusCode == 0");
                    parent.strategyHistory = parent.strategyHistory.slice(0,-1);
                    if(callback)
                        callback(true);
                }else{
                    // delete is not possible
                    $log.error("strategyService.deletePending | statusCode != 0")
                    callback(false);
                }
            });
        }

    }// end return
});
