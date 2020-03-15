import React, { Component } from 'react';
import UserInterface from "../interface/User.interface";

interface UploadState {
    error:boolean;
}




export class PictureUpload extends Component<UserInterface, UploadState>{
    userRef:React.RefObject<HTMLInputElement>;
    constructor(props: UserInterface) {
        super(props);
        this.state ={
            error:false
        };
        this.userRef = React.createRef();
    }
    onUploadClick = (): void =>{
        
    };


}

export default PictureUpload;