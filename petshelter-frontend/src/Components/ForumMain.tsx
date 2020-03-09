import {Component} from "react";
import ForumInterface from "../interface/Forum.interface";

interface IState {
    mode: String
}

interface IProps {

}

export class ForumMain extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
    }
}