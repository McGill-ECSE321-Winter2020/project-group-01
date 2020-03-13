import React, {Component} from 'react';
import {Card} from "@material-ui/core";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";

interface IProps {
    username: string
    email: string,
    amount: string,
    date: string
}

interface IState {

}

//todo: make a card fix sized && more rectangular
class DonationCard extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        //implement props handling
    }

    checkDate(fetchedTime:string):string{
        let fetchedDate = new Date(fetchedTime);
        let today = new Date();
        if(today - fetchedDate ===0){
            return "Today";
        } else if (today - fetchedDate ===1){
            return "Yesterday";
        }
        return fetchedTime; //format the date time object
    }

    render() {
        return (
            <Card className="root" color="textSecondary">
                <CardContent>
                    <Typography variant="h3" component="h2">
                        {this.props.username}
                    </Typography>
                    <Typography variant="h5" component="h2" color="textSecondary">
                        {this.props.email}
                    </Typography>
                    <Typography className="pos" color="textSecondary">
                        {this.props.date}
                    </Typography>
                    <Typography variant="body2" component="p">
                        {this.props.amount}$
                        <br/>
                    </Typography>
                </CardContent>
                <CardActions>
                    <Button
                        size="small"
                        fullWidth
                        variant="contained"
                        color="primary"
                        onClick={() => this.hideDonation}>
                        Hide
                    </Button>
                </CardActions>
            </Card>
        );
    }

    hideDonation(event) {
        //delete self
    }
}


// @ts-ignore
export default DonationCard;