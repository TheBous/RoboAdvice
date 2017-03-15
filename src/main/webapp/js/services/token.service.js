RoboAdviceApp.factory('TokenHandler', function($cookies) {
  tokenByCookie = $cookies.get("token");

  return {
    token : tokenByCookie,
    set : function( newToken ) {
      this.token = newToken;
    },
    get : function() {
      return this.token;
    }
  }
});
