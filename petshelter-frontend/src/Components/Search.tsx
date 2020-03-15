import React, {Component} from 'react';


interface IProps {

}

interface IState {
    searchState?: string
    token: string
}

/*
depending on which state we are in, we will display a different search
I was thinking of a radio style button from the dashboard and people can select what they want to search and it will
return the querry
 */
class Search extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            searchState: 'Donation',
            token: ''
        };
    }

    render() {
        function DonationQuery() {


        }

        return (
            <div>
                {this.state.searchState === 'Donation' && DonationQuery()}
                {this.state.searchState === 'User'}
                {this.state.searchState === 'Advertisement'}
                {this.state.searchState === 'Comment'}
                {this.state.searchState === 'Forum'}
            </div>
        );
    }
}

export default Search;