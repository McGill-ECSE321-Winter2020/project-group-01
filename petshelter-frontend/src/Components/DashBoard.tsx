import React, {Component} from 'react';

interface IProps {

}

interface IState {
    email: string,
    token: string,
    username:string,
    forumPetAdoption?:string
    //maybe the other dashboards / forurm
}

class DashBoard extends Component<IProps, IState> {
    constructor(props:IProps) {
        super(props);
        this.state = {
            email: '',
            token: '',
            username:''
        }
    }

    render(){

        return(
          <div>


          </div>
        );
    }

}

export default DashBoard;