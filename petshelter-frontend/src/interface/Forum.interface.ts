import UserInterface from "./User.interface";
import CommentInterface from "./Comment.interface";

export default interface ForumInterface {
    Author: UserInterface
    [index: number]: CommentInterface
}