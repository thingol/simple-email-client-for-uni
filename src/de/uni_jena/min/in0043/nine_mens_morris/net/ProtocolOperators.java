package de.uni_jena.min.in0043.nine_mens_morris.net;

public class ProtocolOperators {
    public final static byte[] MOVE_STONE          = {1,0,0};
    public final static byte[] REMOVE_STONE        = {2,0,0};
    public final static byte[] CONCEDE             = {3,0,0};
    public final static byte[] BYE                 = {4,0,0};
    public final static byte[] GET_PHASE           = {5,0,0};
    public final static byte[] GET_ACTIVE_PLAYER   = {6,0,0};
    public final static byte[] GET_WHITE_ACTIVATED = {7,0,0};
    public final static byte[] WHITE_IN_PLAY       = {8,0,0};
    public final static byte[] WHITE_LOST          = {9,0,0};
    public final static byte[] GET_BLACK_ACTIVATED = {10,0,0};
    public final static byte[] BLACK_IN_PLAY       = {11,0,0};
    public final static byte[] BLACK_LOS           = {12,0,0};
    public final static byte[] GET_ROUND           = {13,0,0};
    public final static byte[] YOU_WIN             = {14,0,0};
    public final static byte[] YOU_LOSE            = {15,0,0};
    public final static byte[] NEW_GAME            = {16,0,0};
    public final static byte[] NO_MORE             = {17,0,0};
    public final static byte[] IS_WHITE            = {18,0,0};
    public final static byte[] IS_BLACK            = {19,0,0};
    				
    
    public final static byte[] UNKNOW_OP           = {-1,-1,-3};
    public final static byte[] ILLEGAL_OP          = {-1,-1,-2};
    public final static byte[] GENERAL_ERROR       = {-1,-1,-1};
}
