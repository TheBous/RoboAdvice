RoboAdviceApp.component("userEditableFields",{
    bindings: {
        user: "<",
        onUpdate: "&"
    },
    template: `
 <div class="col s12 m7" style="margin: auto;width: 50%;"" >
    <div class="card horizontal">
      <div class="card-image">
      </div>
      <div class="card-stacked">
        <div class="card-content">
          <div class="row" ng-repeat="field in $ctrl.fields">
            <label>{{field.label}}</label>
            <input name="" placeholder="{{field.oldValue}}" ng-model="$ctrl.newUser[field.name]" />
          </div>
          <div class="card-action">
            <div style="text-align: center;"> <a ng-click="$ctrl.update()" class="waves-effect waves-light btn"><i class="material-icons left">save</i>Update</a></div>
          </div>
      </div>
    </div>
  </div>
    `,
    controller: function($log){
        $ctrl = this;

        this.newUser = {}

        this.$onInit = function(){

            for(field in this.user){
                //$log.debug(field);
                $ctrl.newUser[field]=this.user[field]
            }

            $ctrl.fields = [
                {label: "Name",     name:"name",     oldValue: this.user.name},
                {label: "Surname",  name:"surname",  oldValue: this.user.surname}
                //{label: "Email",    name:"email",    oldValue: this.user.email}
            ];
        }

        this.update = function(){
            this.onUpdate({data: [$ctrl.newUser,function(status,message){
              sweetAlert(message, "" , "success");

            }]});
        }
    }
})
