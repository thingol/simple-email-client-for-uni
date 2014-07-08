package de.uni_jena.min.in0043.nine_mens_morris.net;

public class ProtocolOperators {
	// log in and log out
    public final static byte[] HELLO               = {0,0,0};
    public final static byte[] BYE                 = {4,0,0};
    
    /* 
     * MOVE_STONE: regular move, sent by player to server
     * and from server to other player, transfer of state
     * REMOVE_STONE: sent by player after he has created 
     * a mill, relayed to other player by server
     * CONCEDE: player gives up
     */
    public final static byte[] MOVE_STONE          = {1,0,0};
    public final static byte[] REMOVE_STONE        = {2,0,0};
    public final static byte[] CONCEDE             = {3,0,0};
    
    /*
     * all of these are used to request information about the state of the game
     * valid during a running game.
     */
    public final static byte[] GET_ROUND           = {13,0,0};
    public final static byte[] GET_PHASE           = {5,0,0};
    public final static byte[] GET_ACTIVE_PLAYER   = {6,0,0};
    public final static byte[] GET_WHITE_ACTIVATED = {7,0,0};
    public final static byte[] WHITE_IN_PLAY       = {8,0,0};
    public final static byte[] WHITE_LOST          = {9,0,0};
    public final static byte[] GET_BLACK_ACTIVATED = {10,0,0};
    public final static byte[] BLACK_IN_PLAY       = {11,0,0};
    public final static byte[] BLACK_LOST          = {12,0,0};
    
    /*
     * used by the server to indicate game over
     */
    public final static byte[] YOU_WIN             = {14,0,0};
    public final static byte[] YOU_LOSE            = {15,0,0};
    
    
    /*
     * used first by the loser to indicate whether said person wants a new game or not
     * is then relayed to the winner, who then responds with his wish.
     * 
     * if the loser sends a NO_MORE, then that is equivalent to a BYE
     * if the winner sends a NO_MORE, then the server notifies the loser
     * and shuts down the GameServer Thread
     */
    public final static byte[] NEW_GAME            = {16,0,0};
    public final static byte[] NO_MORE             = {17,0,0};
    
    public final static byte[] IS_WHITE            = {18,0,0};
    public final static byte[] IS_BLACK            = {19,0,0};

    /* 
     * sent by both server and players to acknowledge validity of
     * the received message
     */
    public final static byte[] ACK                 = {-2,0,0};
    
    // sent to player who created mill
    public final static byte[] ACK_W_MILL          = {-2,0,1};
    
    /* 
     * sent to player who is about to lose a stone, semantically
     * the same as ACK_w_MILL but easier for programmer to understand
     */
    public final static byte[] MILL_CREATED        = {-2,0,2};
    
    // sent by server to indicate an illegal or impossible move
    public final static byte[] NACK                = {-2,1,0};
//    public final static byte[] ACK_w_mill          = {-2,-1,0};
    
    // should not be needed
    public final static byte[] UNKNOW_OP           = {-1,-1,-3};
    public final static byte[] ILLEGAL_OP          = {-1,-1,-2};
    public final static byte[] GENERAL_ERROR       = {-1,-1,-1};
}
