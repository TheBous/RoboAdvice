/*
 The user service is a singleton for the current logged user.
 You can use it to manage user's data
 */

RoboAdviceApp.service("userService",function($log,$cookies,userREST, $http, TokenHandler, strategyService, portfolioREST, strategyREST, portfolioService, USER_CODES, CONFIG){
    return{
        userObj: {},
        logged: false, // this flag is true when the user is logged
        token: "",

        strategyHistory: null,
        lastStrategy: null,
        hasStrategy: false,

        currentPortfolio: null, // a single portoflio, which has isActive setted, this could be the next portfolio or the portfolio for today
        portfolioHistory: null, // a list of user's portfolios
        portfolioList: null,
        hasPortfolio: false, // this flag is true when the user has at least a portfolio active

        init: function(user_obj){
            let parent = this;
            $log.debug("userService.init| init user called, setted user object");
            this.userObj = user_obj;
            this.logged = true;
            this.setStrategyHistory();
        },
        setStrategyHistory: function(){
          let parent = this;
          $log.debug("userService.setStrategyHistory| setting up user's strategies");

          strategyService.setHistory(this.getId(),function(history){
            if(history != null){
              parent.hasStrategy = true;
              $log.debug("userService.init| the user has " + history.length + " strategies");
            }else{
              // the user don't have strategies
            }

            // setting portfolio History
            parent.setCurrentPortfolio();
          });
        },
        newStrategy: function(strategyObj){
            // insert a new strategy at runtime
            let newStrategy = new Strategy(strategyObj);
            if(this.hasStrategy){
              newStrategy.setFinalAmount(this.getCurrentPortfolioAmount());
              newStrategy.setInitialAmount(this.getCurrentPortfolioAmount());
            }else{
              newStrategy.setInitialAmount(CONFIG["INITIAL_AMOUNT"]);
              newStrategy.setFinalAmount(CONFIG["INITIAL_AMOUNT"]);
              this.hasStrategy = true;
            }
            strategyService.newStrategy(newStrategy);
        },
        setCurrentPortfolio : function() {
          let parent = this;
          portfolioService.getFullHistory(this.getId(), function(portfolioHistory){
            if(portfolioHistory != null){
              // portfolioHistory has something
              $log.debug("userService.setCurrentPortfolio| the user has portfolioHistory")
              parent.currentPortfolio = portfolioHistory[portfolioHistory.length-1];
              parent.hasPortfolio = true;
            }else{
              $log.debug("userService.setCurrentPortfolio| the user doesn't have portfolioHistory");
              parent.hasPortfolio = false;
              // portfolioHistory is empty
            }
          });
        },

        update: function(newObj){
            // this method update the user object with the new object
            $log.info("userService.update| updating the user ");
            $parent = this;
            if(newObj.name == this.userObj.name && newObj.surname==this.userObj.surname){
                // the values are the same, do nothing
                //sweetAlert(USER_CODES["NO_CHANGES"], "", "error")
            }else{
                userREST.update({name: newObj["name"],surname:newObj["surname"],email:$parent.getEmail(),password:$parent.getPassword()}).$promise.then(function(response){
                    if(response.statusCode == 0){
                        // update done
                        $parent.userObj.name = newObj.name;
                        $parent.userObj.surname = newObj.surname;
                        sweetAlert(USER_CODES[response.statusCode], "" , "success");
                    }else{
                        sweetAlert(USER_CODES[response.statusCode], "", "error");
                    }
                });
            }
        },

        /* USER PORTFOLIO METHODS */
        hasCurrentPortfolio: function(){    return this.hasPortfolio;},
        getCurrentPortfolioAmount: function(){
          if(this.currentPortfolio!=null)
            return this.currentPortfolio.getTotalAmount();
          else return CONFIG["INITIAL_AMOUNT"];
        },
        /* USER STRATEGY METHODS */
        hasStrategies: function(){          return this.hasStrategy},

        /* USER METHODS */
        getToken: function(){               return this.token;},
        getId : function(){                 return this.userObj.id; },
        getObj: function(){                 return this.userObj; },
        isLogged: function(){               return this.logged; },
        getUser: function(){                return this.userObj; },
        getName: function(){                return this.userObj.name; },
        getEmail: function(){               return this.userObj.email; },
        getPassword: function(){            return this.userObj.password; },
        getSurname: function(){             return this.userObj.surname; },
        setName: function(name){            this.userObj.name = name; },
        setToken: function(newToken){       this.token=newToken;},
        setEmail: function(email){          this.userObj.email = email; },
        setPassword: function(password){    this.userObj.password = password; },
        setSurname: function(surname){      this.userObj.surname = surname; },
        logout: function(){
            this.userObj = {};
            this.logged = false;
        },

        doLogin: function(params,callback){
            // request the login REST controller from the backend
            let parent = this;
            userREST.login(params).$promise
                .then(function(response){
                    // then is the promise method?
                    // TokenHandler.set("Bearer " + response.data.token);
                    $http.defaults.headers.common['Authorization']= "Bearer " + response.data.token;

                    // $cookies.put("token", "Bearer " + response.data.token);
                    // console.log("-----------------------------")
                    // console.log("Bearer " + response.data.token);
                    // console.log(TokenHandler.get())
                    // console.log("-----------------------------")
                    if(callback != null){
                        // isset the callback, the method wants to handle the response
                        callback(response);
                    }else{
                        // silent login
                        if(data.statusCode == 0){
                            // everything is gone well
                            $log.debug("userService.doLogin silent| statusCode is 0");
                        }else{
                            // TODO. error handler
                            $log.warn("userService.doLogin silent| error");
                            $log.warn("userService.doLogin silent| " + response.statusCode);
                        }
                    }
                });
        }// end login

    }//end service return
})
