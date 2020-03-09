import {Component} from "react";

interface IState {
    mode: String
}

interface IProps {

}

export class ForumList extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
    }
}