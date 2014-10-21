// $ANTLR 3.5.2 cz/vutbr/web/csskit/antlr/CSS.g 2014-07-11 12:43:53

package cz.vutbr.web.csskit.antlr;

import cz.vutbr.web.css.CSSException;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.SupportedCSS;
import org.antlr.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Stack;

@SuppressWarnings("all")
public class CSSLexer extends Lexer {
	public static final int EOF=-1;
	public static final int T__101=101;
	public static final int T__102=102;
	public static final int T__103=103;
	public static final int T__104=104;
	public static final int ADJACENT=4;
	public static final int APOS=5;
	public static final int ASTERISK=6;
	public static final int ATBLOCK=7;
	public static final int ATKEYWORD=8;
	public static final int ATTRIBUTE=9;
	public static final int BRACEBLOCK=10;
	public static final int CDC=11;
	public static final int CDO=12;
	public static final int CHARSET=13;
	public static final int CHILD=14;
	public static final int CLASSKEYWORD=15;
	public static final int COLON=16;
	public static final int COMMA=17;
	public static final int COMMENT=18;
	public static final int CONTAINS=19;
	public static final int CTRL=20;
	public static final int CTRL_CHAR=21;
	public static final int CURLYBLOCK=22;
	public static final int DASHMATCH=23;
	public static final int DECLARATION=24;
	public static final int DESCENDANT=25;
	public static final int DIMENSION=26;
	public static final int ELEMENT=27;
	public static final int ENDSWITH=28;
	public static final int EQUALS=29;
	public static final int ESCAPE_CHAR=30;
	public static final int EXCLAMATION=31;
	public static final int EXPRESSION=32;
	public static final int FONTFACE=33;
	public static final int FUNCTION=34;
	public static final int GREATER=35;
	public static final int HASH=36;
	public static final int IDENT=37;
	public static final int IDENT_MACR=38;
	public static final int IMPORT=39;
	public static final int IMPORTANT=40;
	public static final int INCLUDES=41;
	public static final int INDEX=42;
	public static final int INLINESTYLE=43;
	public static final int INTEGER_MACR=44;
	public static final int INVALID_DECLARATION=45;
	public static final int INVALID_DIRECTIVE=46;
	public static final int INVALID_IMPORT=47;
	public static final int INVALID_SELECTOR=48;
	public static final int INVALID_SELPART=49;
	public static final int INVALID_STATEMENT=50;
	public static final int INVALID_STRING=51;
	public static final int INVALID_TOKEN=52;
	public static final int LBRACE=53;
	public static final int LCURLY=54;
	public static final int LESS=55;
	public static final int LPAREN=56;
	public static final int MARGIN_AREA=57;
	public static final int MEDIA=58;
	public static final int MEDIA_QUERY=59;
	public static final int MINUS=60;
	public static final int NAME_CHAR=61;
	public static final int NAME_MACR=62;
	public static final int NAME_START=63;
	public static final int NL_CHAR=64;
	public static final int NON_ASCII=65;
	public static final int NUMBER=66;
	public static final int NUMBER_MACR=67;
	public static final int PAGE=68;
	public static final int PARENBLOCK=69;
	public static final int PERCENT=70;
	public static final int PERCENTAGE=71;
	public static final int PLUS=72;
	public static final int PRECEDING=73;
	public static final int PSEUDO=74;
	public static final int QUESTION=75;
	public static final int QUOT=76;
	public static final int RBRACE=77;
	public static final int RCURLY=78;
	public static final int RPAREN=79;
	public static final int RULE=80;
	public static final int S=81;
	public static final int SELECTOR=82;
	public static final int SEMICOLON=83;
	public static final int SET=84;
	public static final int SLASH=85;
	public static final int SL_COMMENT=86;
	public static final int STARTSWITH=87;
	public static final int STRING=88;
	public static final int STRING_CHAR=89;
	public static final int STRING_MACR=90;
	public static final int STYLESHEET=91;
	public static final int TILDE=92;
	public static final int UNIRANGE=93;
	public static final int URI=94;
	public static final int URI_CHAR=95;
	public static final int URI_MACR=96;
	public static final int VALUE=97;
	public static final int VIEWPORT=98;
	public static final int W_CHAR=99;
	public static final int W_MACR=100;

	    

	    private static Logger log = LoggerFactory.getLogger(CSSLexer.class);
	    
	    private static SupportedCSS css = CSSFactory.getSupportedCSS();
	    
	    public static class LexerState {
	        
	        public enum RecoveryMode {
	            BALANCED,
	            FUNCTION, 
	            RULE,
	            DECL
	        } 
	    
	        public short curlyNest;
	        public short parenNest;
	        public boolean quotOpen;
	        public boolean aposOpen;
	        
	        public LexerState() {
	        	this.curlyNest = 0;
	        	this.parenNest = 0;
	        	this.quotOpen = false;
	        	this.aposOpen = false;
	        }
	        
	        public LexerState(LexerState clone) {
	        	this();
	            this.curlyNest = clone.curlyNest;
	            this.parenNest = clone.parenNest;
	            this.quotOpen = clone.quotOpen;
	            this.aposOpen = clone.aposOpen;
	        }
	        
	        @Override
	        public boolean equals(Object o)
	        {
	            return (this.curlyNest == ((LexerState) o).curlyNest &&
	                    this.parenNest == ((LexerState) o).parenNest &&
	                    this.quotOpen == ((LexerState) o).quotOpen &&
	                    this.aposOpen == ((LexerState) o).aposOpen);
	        }
	        
	        /**
	         * Checks whether all pair characters (single and double quotatation marks,
	         * curly braces) are balanced
			     */ 
	        public boolean isBalanced() {
	        	return aposOpen==false && quotOpen==false && curlyNest==0 && parenNest==0;
	        }
	        
	        /**
	         * Checks whether some pair characters are balanced. Modes are:
	         * <ul>
	         * <li>BALANCED - everything must be balanced: single and double quotatation marks, curly braces, parentheses 
	         * <li>FUNCTION - within the function arguments: parentheses must be balanced 
	         * <li>RULE - within the CSS rule: all but curly braces
	         * <li>DECL - within declaration: all, keep curly braces at desired state
	         * </ul>
	         * @param mode the desired recovery node
	         * @param state the required lexer state (used for DECL mode)
	         * @param t the token that is being processed (used for DECL mode)
	         */ 
	        public boolean isBalanced(RecoveryMode mode, LexerState state, CSSToken t)
	        {
	            if (mode == RecoveryMode.BALANCED)
	                return aposOpen==false && quotOpen==false && curlyNest==0 && parenNest==0;
	            else if (mode == RecoveryMode.FUNCTION)
	                return parenNest==0;
	            else if (mode == RecoveryMode.RULE)
	                return aposOpen==false && quotOpen==false && parenNest==0;
	            else if (mode == RecoveryMode.DECL)
	            {
	                if (t.getType() == RCURLY) //if '}' is processed the curlyNest has been already decreased 
	                    return aposOpen==false && quotOpen==false && parenNest==0 && curlyNest==state.curlyNest-1;
	                else
	                    return aposOpen==false && quotOpen==false && parenNest==0 && curlyNest==state.curlyNest;
	            }
	            else
	                return false;
	        }
	        
	        /**
	         * Recovers from unexpected EOF by preparing 
	         * new token
	         */ 
	        public CSSToken generateEOFRecover() {
	        	
	        	CSSToken t = null;
	        
	        	if(aposOpen) {
	        		this.aposOpen=false;
	        		t = new CSSToken(CSSLexer.APOS, this);
	        		t.setText("'");
	        	}
	        	else if(quotOpen) {
	        		this.quotOpen=false;
	        		t = new CSSToken(CSSLexer.QUOT, this);
	        		t.setText("\"");
	        	}
	        	else if(parenNest!=0) {
	        		this.parenNest--;
	        		t = new CSSToken(CSSLexer.RPAREN, this);
	        		t.setText(")");
	        	}
	        	else if(curlyNest!=0) {
	        		this.curlyNest--;
	        		t = new CSSToken(CSSLexer.RCURLY, this);
	        		t.setText("}");
	        	}
	        	
	        	log.debug("Recovering from EOF by {}", t);
	        	return t;
	        }
	        
	        @Override
	        public String toString() {
	        	StringBuilder sb = new StringBuilder();
	        	sb.append("{=").append(curlyNest)
	        	  .append(", (=").append(parenNest)
	        	  .append(", '=").append(aposOpen ? "1" : "0")
	        	  .append(", \"=").append(quotOpen ? "1" :"0");
	        	 
	        	return sb.toString();  
	        }
	    }
	    
	    private static class LexerStream {
	    
	    	public CharStream input;
	    	public int mark;
	    	public LexerState ls;
	    	
	    	public LexerStream(CharStream input, LexerState ls) {
	    	    this.input = input;
	    	    this.mark = input.mark();
	    	    this.ls = new LexerState(ls);
	    	}
	    }
	    
	    // number of already processed tokens (for checking the beginning of the style sheet)
	    private int tokencnt = 0;
	    
	    // lexer states for imported files
	    private Stack<LexerStream> imports;
	    
	    // current lexer state
	    private LexerState ls;
	    
	    // token recovery
	    private Stack<Integer> expectedToken;
	    
	    /**
	     * This function must be called to initialize lexer's state.
	     * Because we can't change directly generated constructors.
	     */
	    public CSSLexer init() {
		    this.imports = new Stack<LexerStream>();
		    this.expectedToken = new Stack<Integer>();
		    this.ls = new LexerState();
		    return this;
	    }
	    
	    @Override
	    public void reset() {
	    	super.reset();
	    	this.ls = new LexerState();
	    }
	    
	    /**
	     * Overrides next token to match includes and to 
	     * recover from EOF
	     */
		@Override 
	    public Token nextToken(){
	       Token token = nextTokenRecover();

	       //count non-empty tokens for eventual checking of the style sheet start
	       if (token.getType() == S) {
	           tokencnt++;
	       }

	       // recover from unexpected EOF
	       if(token.getType()==Token.EOF && !ls.isBalanced()) {
	           CSSToken t = ls.generateEOFRecover(); 
	           return (Token) t;
	       }

	       // Skip first token after switching on another input.
	       if(((CommonToken)token).getStartIndex() < 0)
	         token = nextToken();
	        
	       return token;
	    }

	    /**
		 * Adds contextual information about n      {
		 esting into token.
		 */
	    @Override
		public Token emit() {
			CSSToken t = new CSSToken(input, state.type, state.channel,
	                        state.tokenStartCharIndex, getCharIndex()-1);
	        t.setLine(state.tokenStartLine);
	        t.setText(state.text);
	        t.setCharPositionInLine(state.tokenStartCharPositionInLine);
	        t.setBase(((CSSInputStream) input).getBase());
	        
	        // clone lexer state
	        t.setLexerState(new LexerState(ls));
	        emit(t);
	        return t;
		}

		@Override
	    public void emitErrorMessage(String msg) {
	    	log.info("ANTLR: {}", msg);
	    }
	    
	    /**
	     * Does special token recovery for some cases
	     */ 
	    @Override
	    public void recover(RecognitionException re) {
	    	// there is no special recovery
	    	if(expectedToken.isEmpty())
	    		super.recover(re);
	    	else {
	    		switch(expectedToken.pop().intValue()) {
	    		
	    		case IMPORT:  // IMPORT share recovery rules with CHARSET
	    		case CHARSET:
	    			final BitSet charsetFollow = BitSet.of((int) '}', (int) ';');
	    			consumeUntilBalanced(charsetFollow);
	    			break;
	    		case STRING:
	    			// eat character which should be newline but not EOF
	    			if(consumeAnyButEOF()) {
	    				// set back to uninitialized state
	    				ls.quotOpen = false;
	    				ls.aposOpen = false;
	    				// create invalid string token
	    				state.token = (Token) new CSSToken(INVALID_STRING, ls);
	        			state.token.setText("INVALID_STRING");
	    			}
	    			// we can't just let parser generate missing 
	    		    // single/double quot token
	    			// because we have not emitted content of string yet!
	    			// we will fake string token
	    			else {
	    				char enclosing = (ls.quotOpen) ? '"' : '\'';
	    				ls.quotOpen = false;
	    				ls.aposOpen = false;
	    				state.token = (Token) new CSSToken(STRING, ls, 
	    					state.tokenStartCharIndex, getCharIndex() -1);
	        			state.token.setText(
	        				input.substring(state.tokenStartCharIndex, getCharIndex() -1)
	        				+ enclosing);	
	    			}
	    			break;
	    		default:
	    			super.recover(re);
	    		}
	    	}	
	    }
	    
	    /**
	     * Implements Lexer's next token with extra token passing from
	     * recovery function 
	     */
	    private Token nextTokenRecover() {
	    	while (true) {
				state.token = null;
				state.channel = Token.DEFAULT_CHANNEL;
				state.tokenStartCharIndex = input.index();
				state.tokenStartCharPositionInLine = input.getCharPositionInLine();
				state.tokenStartLine = input.getLine();
				state.text = null;
				if ( input.LA(1)==CharStream.EOF ) {
					return getEOFToken();
				}
				try {
					mTokens();
					if ( state.token==null ) {
						emit();
					}
					else if ( state.token==Token.SKIP_TOKEN ) {
						continue;
					}
					return state.token;
				}
				catch (RecognitionException re) {
					reportError(re);
					if ( re instanceof NoViableAltException ) {
						recover(re); 
					}

					// there can be token returned from recovery
	                if(state.token!=null) {
	                    state.token.setChannel(Token.INVALID_TOKEN_TYPE);
	                  	state.token.setInputStream(input);
	                   	state.token.setLine(state.tokenStartLine);
	            		state.token.setCharPositionInLine(state.tokenStartCharPositionInLine);
	            		emit(state.token);
	            		return state.token;
	                }
				}
			}
		}
	    
	    /**
	     * Eats characters until on from follow is found and Lexer state 
	     * is balanced at the moment
	     */ 
	    private void consumeUntilBalanced(BitSet follow) {

	    	log.debug("Lexer entered consumeUntilBalanced with {} and follow {}", 
	    		ls, follow);
	    
	    	int c;
			do {
	    		c = input.LA(1);
	    		// change apostrophe state
	    		if(c=='\'' && ls.quotOpen==false) {
	    			ls.aposOpen = !ls.aposOpen;
	    		}
	    		// change quot state
	    		else if (c=='"' && ls.aposOpen==false) {
	    			ls.quotOpen = !ls.quotOpen;
	    		}
	    		else if(c=='(') {
	    			ls.parenNest++;
	    		}
	    		else if(c==')' && ls.parenNest>0) {
	    			ls.parenNest--;
	    		}
	    		else if(c=='{') {
	    			ls.curlyNest++;
	    		}
	    		else if(c=='}' && ls.curlyNest>0) {
	    			ls.curlyNest--;
	    		}
	    		// handle end of line in string
	    		else if(c=='\n') {
	    			if(ls.quotOpen) ls.quotOpen=false;
	    			else if(ls.aposOpen) ls.aposOpen=false;
	    		} 
	    		else if(c==EOF) {
	    			log.info("Unexpected EOF during consumeUntilBalanced, EOF not consumed");
	    			return;
	    		}
	    	
	    		input.consume();
	    		// log result
	    		if(log.isTraceEnabled())
	    			log.trace("Lexer consumes '{}'({}) until balanced ({}).", 
	    				new Object[] {
	    					Character.toString((char) c),
	    					Integer.toString(c),
	    					ls});
	    			
	    	}while(!(ls.isBalanced() && follow.member(c)));
	    }
	    
	    /**
	     * Consumes arbitrary character but EOF
	     * @return <code>false</code> if EOF was matched,
	     *         <code>true</code> otherwise and that character was consumed
	     */
	    private boolean consumeAnyButEOF() {
	    
	    	int c = input.LA(1);
	    	
	    	if(c==CharStream.EOF)
	    		return false;
	    		
	    	if(log.isTraceEnabled())
	    		log.trace("Lexer consumes '{}' while consumeButEOF", 
	    					Character.toString((char)c));
	    	
	    	// consume character				
	    	input.consume();
	    	return true;
	    }
	    
	    /**
	     * Reads all the contents of an expression. Parenthesis are matched but not in strings.
	     */ 
	    private String readExpressionContents() 
	    {
	      log.debug("readExpressionContents"); 
	    
	      StringBuffer ret = new StringBuffer(); 
	      int parenN = 1; /* one already open */
	      boolean inApos = false;
	      boolean inQuot = false;
	      boolean esc = false;
	      boolean finished = false;
	      int c;
		    while (!finished)
		    {
		        c = input.LA(1);
		        if(c=='\'' && !inQuot && !(inApos && esc)) {
		            inApos = !inApos;
		        }
		        else if (c=='"' && !inApos && !(inQuot && esc)) {
		            inQuot = !inQuot;
		        }
		        else if(c=='(' && !inApos && !inQuot) {
		            parenN++;
		        }
		        else if(c==')' && parenN>0  && !inApos && !inQuot) {
		            parenN--;
		            if (parenN == 0) finished = true;
		        }
		        // handle end of line in string
		        else if (c=='\n') {
		           inQuot = false;
		           inApos = false;
		        } 
		        else if(c==EOF) {
		          log.info("Unexpected EOF during consumeUntilBalanced, EOF not consumed");
		          return ret.toString();
		        }
		        
		        esc = (c == '\\') && !esc;
		        
		        if (!finished) ret.append((char) c);
		        
		        input.consume();
		          
		    }
		    
		    log.debug("Expr: " + ret.toString());
	      return ret.toString();
	    }
	    


	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public CSSLexer() {} 
	public CSSLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public CSSLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "cz/vutbr/web/csskit/antlr/CSS.g"; }

	// $ANTLR start "T__101"
	public final void mT__101() throws RecognitionException {
		try {
			int _type = T__101;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:462:8: ( '#' )
			// cz/vutbr/web/csskit/antlr/CSS.g:462:10: '#'
			{
			match('#'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__101"

	// $ANTLR start "T__102"
	public final void mT__102() throws RecognitionException {
		try {
			int _type = T__102;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:463:8: ( '&' )
			// cz/vutbr/web/csskit/antlr/CSS.g:463:10: '&'
			{
			match('&'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__102"

	// $ANTLR start "T__103"
	public final void mT__103() throws RecognitionException {
		try {
			int _type = T__103;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:464:8: ( '^' )
			// cz/vutbr/web/csskit/antlr/CSS.g:464:10: '^'
			{
			match('^'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__103"

	// $ANTLR start "T__104"
	public final void mT__104() throws RecognitionException {
		try {
			int _type = T__104;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:465:8: ( 'important' )
			// cz/vutbr/web/csskit/antlr/CSS.g:465:10: 'important'
			{
			match("important"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__104"

	// $ANTLR start "IDENT"
	public final void mIDENT() throws RecognitionException {
		try {
			int _type = IDENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1124:2: ( IDENT_MACR )
			// cz/vutbr/web/csskit/antlr/CSS.g:1124:4: IDENT_MACR
			{
			mIDENT_MACR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IDENT"

	// $ANTLR start "CHARSET"
	public final void mCHARSET() throws RecognitionException {
		try {
			int _type = CHARSET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			CommonToken s=null;


				expectedToken.push(new Integer(CHARSET));

			// cz/vutbr/web/csskit/antlr/CSS.g:1134:2: ( '@charset' ( S )* s= STRING_MACR ( S )* SEMICOLON )
			// cz/vutbr/web/csskit/antlr/CSS.g:1134:4: '@charset' ( S )* s= STRING_MACR ( S )* SEMICOLON
			{
			match("@charset"); 

			// cz/vutbr/web/csskit/antlr/CSS.g:1134:15: ( S )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '\t' && LA1_0 <= '\r')||LA1_0==' ') ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1134:15: S
					{
					mS(); 

					}
					break;

				default :
					break loop1;
				}
			}

			int sStart89 = getCharIndex();
			int sStartLine89 = getLine();
			int sStartCharPos89 = getCharPositionInLine();
			mSTRING_MACR(); 
			s = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, sStart89, getCharIndex()-1);
			s.setLine(sStartLine89);
			s.setCharPositionInLine(sStartCharPos89);

			// cz/vutbr/web/csskit/antlr/CSS.g:1134:32: ( S )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '\t' && LA2_0 <= '\r')||LA2_0==' ') ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1134:32: S
					{
					mS(); 

					}
					break;

				default :
					break loop2;
				}
			}

			mSEMICOLON(); 


				    // we have to trim manually
				    String enc = CSSToken.extractSTRING(s.getText());
				    //System.err.println("CHARSET"+tokencnt);
				    if (tokencnt <= 1) //we are at the beginning of the style sheet
				    {
						    try {
						           log.warn("Changing charset to {}", enc);
						          ((CSSInputStream) input).setEncoding(enc);
						          //input = setCharStream(new ANTLFileStream(input.getSourceName(), enc));
						        }
						        catch(IllegalCharsetNameException icne) {
						        	log.warn("Could not change to unsupported charset!", icne);
						        	throw new RuntimeException(new CSSException("Unsupported charset: " + enc));
						        }
						        catch (IOException e) {
			                log.warn("Could not change to unsupported charset!", e);
						        }
						 }
						 else
						      log.warn("Ignoring @charset rule not at the beginning of the style sheet");
				  
			}

			state.type = _type;
			state.channel = _channel;

				expectedToken.pop();

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CHARSET"

	// $ANTLR start "IMPORT"
	public final void mIMPORT() throws RecognitionException {
		try {
			int _type = IMPORT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1160:2: ( '@import' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1160:4: '@import'
			{
			match("@import"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IMPORT"

	// $ANTLR start "MEDIA"
	public final void mMEDIA() throws RecognitionException {
		try {
			int _type = MEDIA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1164:2: ( '@media' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1164:4: '@media'
			{
			match("@media"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MEDIA"

	// $ANTLR start "PAGE"
	public final void mPAGE() throws RecognitionException {
		try {
			int _type = PAGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1168:2: ( '@page' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1168:4: '@page'
			{
			match("@page"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PAGE"

	// $ANTLR start "MARGIN_AREA"
	public final void mMARGIN_AREA() throws RecognitionException {
		try {
			int _type = MARGIN_AREA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1172:3: ( '@top-left-corner' | '@top-left' | '@top-center' | '@top-right' | '@top-right-corner' | '@bottom-left-corner' | '@bottom-left' | '@bottom-center' | '@bottom-right' | '@bottom-right-corner' | '@left-top' | '@left-middle' | '@left-bottom' | '@right-top' | '@right-middle' | '@right-bottom' )
			int alt3=16;
			int LA3_0 = input.LA(1);
			if ( (LA3_0=='@') ) {
				switch ( input.LA(2) ) {
				case 't':
					{
					int LA3_2 = input.LA(3);
					if ( (LA3_2=='o') ) {
						int LA3_6 = input.LA(4);
						if ( (LA3_6=='p') ) {
							int LA3_10 = input.LA(5);
							if ( (LA3_10=='-') ) {
								switch ( input.LA(6) ) {
								case 'l':
									{
									int LA3_18 = input.LA(7);
									if ( (LA3_18=='e') ) {
										int LA3_24 = input.LA(8);
										if ( (LA3_24=='f') ) {
											int LA3_31 = input.LA(9);
											if ( (LA3_31=='t') ) {
												int LA3_37 = input.LA(10);
												if ( (LA3_37=='-') ) {
													alt3=1;
												}

												else {
													alt3=2;
												}

											}

											else {
												int nvaeMark = input.mark();
												try {
													for (int nvaeConsume = 0; nvaeConsume < 9 - 1; nvaeConsume++) {
														input.consume();
													}
													NoViableAltException nvae =
														new NoViableAltException("", 3, 31, input);
													throw nvae;
												} finally {
													input.rewind(nvaeMark);
												}
											}

										}

										else {
											int nvaeMark = input.mark();
											try {
												for (int nvaeConsume = 0; nvaeConsume < 8 - 1; nvaeConsume++) {
													input.consume();
												}
												NoViableAltException nvae =
													new NoViableAltException("", 3, 24, input);
												throw nvae;
											} finally {
												input.rewind(nvaeMark);
											}
										}

									}

									else {
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++) {
												input.consume();
											}
											NoViableAltException nvae =
												new NoViableAltException("", 3, 18, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}

									}
									break;
								case 'c':
									{
									alt3=3;
									}
									break;
								case 'r':
									{
									int LA3_20 = input.LA(7);
									if ( (LA3_20=='i') ) {
										int LA3_25 = input.LA(8);
										if ( (LA3_25=='g') ) {
											int LA3_32 = input.LA(9);
											if ( (LA3_32=='h') ) {
												int LA3_38 = input.LA(10);
												if ( (LA3_38=='t') ) {
													int LA3_44 = input.LA(11);
													if ( (LA3_44=='-') ) {
														alt3=5;
													}

													else {
														alt3=4;
													}

												}

												else {
													int nvaeMark = input.mark();
													try {
														for (int nvaeConsume = 0; nvaeConsume < 10 - 1; nvaeConsume++) {
															input.consume();
														}
														NoViableAltException nvae =
															new NoViableAltException("", 3, 38, input);
														throw nvae;
													} finally {
														input.rewind(nvaeMark);
													}
												}

											}

											else {
												int nvaeMark = input.mark();
												try {
													for (int nvaeConsume = 0; nvaeConsume < 9 - 1; nvaeConsume++) {
														input.consume();
													}
													NoViableAltException nvae =
														new NoViableAltException("", 3, 32, input);
													throw nvae;
												} finally {
													input.rewind(nvaeMark);
												}
											}

										}

										else {
											int nvaeMark = input.mark();
											try {
												for (int nvaeConsume = 0; nvaeConsume < 8 - 1; nvaeConsume++) {
													input.consume();
												}
												NoViableAltException nvae =
													new NoViableAltException("", 3, 25, input);
												throw nvae;
											} finally {
												input.rewind(nvaeMark);
											}
										}

									}

									else {
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++) {
												input.consume();
											}
											NoViableAltException nvae =
												new NoViableAltException("", 3, 20, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}

									}
									break;
								default:
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++) {
											input.consume();
										}
										NoViableAltException nvae =
											new NoViableAltException("", 3, 14, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}
							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 3, 10, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 6, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case 'b':
					{
					int LA3_3 = input.LA(3);
					if ( (LA3_3=='o') ) {
						int LA3_7 = input.LA(4);
						if ( (LA3_7=='t') ) {
							int LA3_11 = input.LA(5);
							if ( (LA3_11=='t') ) {
								int LA3_15 = input.LA(6);
								if ( (LA3_15=='o') ) {
									int LA3_21 = input.LA(7);
									if ( (LA3_21=='m') ) {
										int LA3_26 = input.LA(8);
										if ( (LA3_26=='-') ) {
											switch ( input.LA(9) ) {
											case 'l':
												{
												int LA3_39 = input.LA(10);
												if ( (LA3_39=='e') ) {
													int LA3_45 = input.LA(11);
													if ( (LA3_45=='f') ) {
														int LA3_49 = input.LA(12);
														if ( (LA3_49=='t') ) {
															int LA3_51 = input.LA(13);
															if ( (LA3_51=='-') ) {
																alt3=6;
															}

															else {
																alt3=7;
															}

														}

														else {
															int nvaeMark = input.mark();
															try {
																for (int nvaeConsume = 0; nvaeConsume < 12 - 1; nvaeConsume++) {
																	input.consume();
																}
																NoViableAltException nvae =
																	new NoViableAltException("", 3, 49, input);
																throw nvae;
															} finally {
																input.rewind(nvaeMark);
															}
														}

													}

													else {
														int nvaeMark = input.mark();
														try {
															for (int nvaeConsume = 0; nvaeConsume < 11 - 1; nvaeConsume++) {
																input.consume();
															}
															NoViableAltException nvae =
																new NoViableAltException("", 3, 45, input);
															throw nvae;
														} finally {
															input.rewind(nvaeMark);
														}
													}

												}

												else {
													int nvaeMark = input.mark();
													try {
														for (int nvaeConsume = 0; nvaeConsume < 10 - 1; nvaeConsume++) {
															input.consume();
														}
														NoViableAltException nvae =
															new NoViableAltException("", 3, 39, input);
														throw nvae;
													} finally {
														input.rewind(nvaeMark);
													}
												}

												}
												break;
											case 'c':
												{
												alt3=8;
												}
												break;
											case 'r':
												{
												int LA3_41 = input.LA(10);
												if ( (LA3_41=='i') ) {
													int LA3_46 = input.LA(11);
													if ( (LA3_46=='g') ) {
														int LA3_50 = input.LA(12);
														if ( (LA3_50=='h') ) {
															int LA3_52 = input.LA(13);
															if ( (LA3_52=='t') ) {
																int LA3_55 = input.LA(14);
																if ( (LA3_55=='-') ) {
																	alt3=10;
																}

																else {
																	alt3=9;
																}

															}

															else {
																int nvaeMark = input.mark();
																try {
																	for (int nvaeConsume = 0; nvaeConsume < 13 - 1; nvaeConsume++) {
																		input.consume();
																	}
																	NoViableAltException nvae =
																		new NoViableAltException("", 3, 52, input);
																	throw nvae;
																} finally {
																	input.rewind(nvaeMark);
																}
															}

														}

														else {
															int nvaeMark = input.mark();
															try {
																for (int nvaeConsume = 0; nvaeConsume < 12 - 1; nvaeConsume++) {
																	input.consume();
																}
																NoViableAltException nvae =
																	new NoViableAltException("", 3, 50, input);
																throw nvae;
															} finally {
																input.rewind(nvaeMark);
															}
														}

													}

													else {
														int nvaeMark = input.mark();
														try {
															for (int nvaeConsume = 0; nvaeConsume < 11 - 1; nvaeConsume++) {
																input.consume();
															}
															NoViableAltException nvae =
																new NoViableAltException("", 3, 46, input);
															throw nvae;
														} finally {
															input.rewind(nvaeMark);
														}
													}

												}

												else {
													int nvaeMark = input.mark();
													try {
														for (int nvaeConsume = 0; nvaeConsume < 10 - 1; nvaeConsume++) {
															input.consume();
														}
														NoViableAltException nvae =
															new NoViableAltException("", 3, 41, input);
														throw nvae;
													} finally {
														input.rewind(nvaeMark);
													}
												}

												}
												break;
											default:
												int nvaeMark = input.mark();
												try {
													for (int nvaeConsume = 0; nvaeConsume < 9 - 1; nvaeConsume++) {
														input.consume();
													}
													NoViableAltException nvae =
														new NoViableAltException("", 3, 33, input);
													throw nvae;
												} finally {
													input.rewind(nvaeMark);
												}
											}
										}

										else {
											int nvaeMark = input.mark();
											try {
												for (int nvaeConsume = 0; nvaeConsume < 8 - 1; nvaeConsume++) {
													input.consume();
												}
												NoViableAltException nvae =
													new NoViableAltException("", 3, 26, input);
												throw nvae;
											} finally {
												input.rewind(nvaeMark);
											}
										}

									}

									else {
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++) {
												input.consume();
											}
											NoViableAltException nvae =
												new NoViableAltException("", 3, 21, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}

								}

								else {
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++) {
											input.consume();
										}
										NoViableAltException nvae =
											new NoViableAltException("", 3, 15, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}

							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 3, 11, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case 'l':
					{
					int LA3_4 = input.LA(3);
					if ( (LA3_4=='e') ) {
						int LA3_8 = input.LA(4);
						if ( (LA3_8=='f') ) {
							int LA3_12 = input.LA(5);
							if ( (LA3_12=='t') ) {
								int LA3_16 = input.LA(6);
								if ( (LA3_16=='-') ) {
									switch ( input.LA(7) ) {
									case 't':
										{
										alt3=11;
										}
										break;
									case 'm':
										{
										alt3=12;
										}
										break;
									case 'b':
										{
										alt3=13;
										}
										break;
									default:
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++) {
												input.consume();
											}
											NoViableAltException nvae =
												new NoViableAltException("", 3, 22, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}
								}

								else {
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++) {
											input.consume();
										}
										NoViableAltException nvae =
											new NoViableAltException("", 3, 16, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}

							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 3, 12, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 8, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case 'r':
					{
					int LA3_5 = input.LA(3);
					if ( (LA3_5=='i') ) {
						int LA3_9 = input.LA(4);
						if ( (LA3_9=='g') ) {
							int LA3_13 = input.LA(5);
							if ( (LA3_13=='h') ) {
								int LA3_17 = input.LA(6);
								if ( (LA3_17=='t') ) {
									int LA3_23 = input.LA(7);
									if ( (LA3_23=='-') ) {
										switch ( input.LA(8) ) {
										case 't':
											{
											alt3=14;
											}
											break;
										case 'm':
											{
											alt3=15;
											}
											break;
										case 'b':
											{
											alt3=16;
											}
											break;
										default:
											int nvaeMark = input.mark();
											try {
												for (int nvaeConsume = 0; nvaeConsume < 8 - 1; nvaeConsume++) {
													input.consume();
												}
												NoViableAltException nvae =
													new NoViableAltException("", 3, 30, input);
												throw nvae;
											} finally {
												input.rewind(nvaeMark);
											}
										}
									}

									else {
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++) {
												input.consume();
											}
											NoViableAltException nvae =
												new NoViableAltException("", 3, 23, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}

								}

								else {
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++) {
											input.consume();
										}
										NoViableAltException nvae =
											new NoViableAltException("", 3, 17, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}

							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 3, 13, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 3, 9, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 3, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}

			switch (alt3) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1172:5: '@top-left-corner'
					{
					match("@top-left-corner"); 

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1173:5: '@top-left'
					{
					match("@top-left"); 

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1174:5: '@top-center'
					{
					match("@top-center"); 

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1175:5: '@top-right'
					{
					match("@top-right"); 

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1176:5: '@top-right-corner'
					{
					match("@top-right-corner"); 

					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1177:5: '@bottom-left-corner'
					{
					match("@bottom-left-corner"); 

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1178:5: '@bottom-left'
					{
					match("@bottom-left"); 

					}
					break;
				case 8 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1179:5: '@bottom-center'
					{
					match("@bottom-center"); 

					}
					break;
				case 9 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1180:5: '@bottom-right'
					{
					match("@bottom-right"); 

					}
					break;
				case 10 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1181:5: '@bottom-right-corner'
					{
					match("@bottom-right-corner"); 

					}
					break;
				case 11 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1182:5: '@left-top'
					{
					match("@left-top"); 

					}
					break;
				case 12 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1183:5: '@left-middle'
					{
					match("@left-middle"); 

					}
					break;
				case 13 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1184:5: '@left-bottom'
					{
					match("@left-bottom"); 

					}
					break;
				case 14 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1185:5: '@right-top'
					{
					match("@right-top"); 

					}
					break;
				case 15 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1186:5: '@right-middle'
					{
					match("@right-middle"); 

					}
					break;
				case 16 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1187:5: '@right-bottom'
					{
					match("@right-bottom"); 

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MARGIN_AREA"

	// $ANTLR start "VIEWPORT"
	public final void mVIEWPORT() throws RecognitionException {
		try {
			int _type = VIEWPORT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1191:3: ( '@viewport' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1191:5: '@viewport'
			{
			match("@viewport"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "VIEWPORT"

	// $ANTLR start "FONTFACE"
	public final void mFONTFACE() throws RecognitionException {
		try {
			int _type = FONTFACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1195:3: ( '@font-face' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1195:5: '@font-face'
			{
			match("@font-face"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FONTFACE"

	// $ANTLR start "ATKEYWORD"
	public final void mATKEYWORD() throws RecognitionException {
		try {
			int _type = ATKEYWORD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1201:2: ( '@' ( MINUS )? ( IDENT_MACR )? )
			// cz/vutbr/web/csskit/antlr/CSS.g:1201:4: '@' ( MINUS )? ( IDENT_MACR )?
			{
			match('@'); 
			// cz/vutbr/web/csskit/antlr/CSS.g:1201:8: ( MINUS )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0=='-') ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:
					{
					if ( input.LA(1)=='-' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			// cz/vutbr/web/csskit/antlr/CSS.g:1201:15: ( IDENT_MACR )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( ((LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='\\'||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')||(LA5_0 >= '\u0080' && LA5_0 <= '\uD7FF')||(LA5_0 >= '\uE000' && LA5_0 <= '\uFFFD')) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1201:15: IDENT_MACR
					{
					mIDENT_MACR(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ATKEYWORD"

	// $ANTLR start "CLASSKEYWORD"
	public final void mCLASSKEYWORD() throws RecognitionException {
		try {
			int _type = CLASSKEYWORD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1204:5: ( '.' IDENT_MACR )
			// cz/vutbr/web/csskit/antlr/CSS.g:1204:7: '.' IDENT_MACR
			{
			match('.'); 
			mIDENT_MACR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLASSKEYWORD"

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException {
		try {
			int _type = STRING;
			int _channel = DEFAULT_TOKEN_CHANNEL;

				expectedToken.push(new Integer(STRING));

			// cz/vutbr/web/csskit/antlr/CSS.g:1216:2: ( STRING_MACR )
			// cz/vutbr/web/csskit/antlr/CSS.g:1216:4: STRING_MACR
			{
			mSTRING_MACR(); 

			}

			state.type = _type;
			state.channel = _channel;

				expectedToken.pop();

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING"

	// $ANTLR start "HASH"
	public final void mHASH() throws RecognitionException {
		try {
			int _type = HASH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1221:2: ( '#' NAME_MACR )
			// cz/vutbr/web/csskit/antlr/CSS.g:1221:4: '#' NAME_MACR
			{
			match('#'); 
			mNAME_MACR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HASH"

	// $ANTLR start "INDEX"
	public final void mINDEX() throws RecognitionException {
		try {
			int _type = INDEX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1226:3: ( ( INTEGER_MACR )? ( 'N' | 'n' ) ( ( S )* ( PLUS | MINUS ) ( S )* INTEGER_MACR )? )
			// cz/vutbr/web/csskit/antlr/CSS.g:1226:5: ( INTEGER_MACR )? ( 'N' | 'n' ) ( ( S )* ( PLUS | MINUS ) ( S )* INTEGER_MACR )?
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1226:5: ( INTEGER_MACR )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1226:5: INTEGER_MACR
					{
					mINTEGER_MACR(); 

					}
					break;

			}

			if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// cz/vutbr/web/csskit/antlr/CSS.g:1226:31: ( ( S )* ( PLUS | MINUS ) ( S )* INTEGER_MACR )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( ((LA9_0 >= '\t' && LA9_0 <= '\r')||LA9_0==' '||LA9_0=='+'||LA9_0=='-') ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1226:32: ( S )* ( PLUS | MINUS ) ( S )* INTEGER_MACR
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:1226:32: ( S )*
					loop7:
					while (true) {
						int alt7=2;
						int LA7_0 = input.LA(1);
						if ( ((LA7_0 >= '\t' && LA7_0 <= '\r')||LA7_0==' ') ) {
							alt7=1;
						}

						switch (alt7) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1226:32: S
							{
							mS(); 

							}
							break;

						default :
							break loop7;
						}
					}

					if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					// cz/vutbr/web/csskit/antlr/CSS.g:1226:50: ( S )*
					loop8:
					while (true) {
						int alt8=2;
						int LA8_0 = input.LA(1);
						if ( ((LA8_0 >= '\t' && LA8_0 <= '\r')||LA8_0==' ') ) {
							alt8=1;
						}

						switch (alt8) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1226:50: S
							{
							mS(); 

							}
							break;

						default :
							break loop8;
						}
					}

					mINTEGER_MACR(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INDEX"

	// $ANTLR start "NUMBER"
	public final void mNUMBER() throws RecognitionException {
		try {
			int _type = NUMBER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1231:2: ( NUMBER_MACR )
			// cz/vutbr/web/csskit/antlr/CSS.g:1231:4: NUMBER_MACR
			{
			mNUMBER_MACR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMBER"

	// $ANTLR start "PERCENTAGE"
	public final void mPERCENTAGE() throws RecognitionException {
		try {
			int _type = PERCENTAGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1236:2: ( NUMBER_MACR '%' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1236:4: NUMBER_MACR '%'
			{
			mNUMBER_MACR(); 

			match('%'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PERCENTAGE"

	// $ANTLR start "DIMENSION"
	public final void mDIMENSION() throws RecognitionException {
		try {
			int _type = DIMENSION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1241:2: ( NUMBER_MACR IDENT_MACR )
			// cz/vutbr/web/csskit/antlr/CSS.g:1241:4: NUMBER_MACR IDENT_MACR
			{
			mNUMBER_MACR(); 

			mIDENT_MACR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIMENSION"

	// $ANTLR start "URI"
	public final void mURI() throws RecognitionException {
		try {
			int _type = URI;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1246:2: ( 'url(' W_MACR ( STRING_MACR | URI_MACR ) W_MACR ')' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1246:4: 'url(' W_MACR ( STRING_MACR | URI_MACR ) W_MACR ')'
			{
			match("url("); 

			mW_MACR(); 

			// cz/vutbr/web/csskit/antlr/CSS.g:1246:18: ( STRING_MACR | URI_MACR )
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0=='\"'||LA10_0=='\'') ) {
				alt10=1;
			}
			else if ( ((LA10_0 >= '\t' && LA10_0 <= '\r')||(LA10_0 >= ' ' && LA10_0 <= '!')||(LA10_0 >= '#' && LA10_0 <= '&')||(LA10_0 >= ')' && LA10_0 <= '~')||(LA10_0 >= '\u0080' && LA10_0 <= '\uD7FF')||(LA10_0 >= '\uE000' && LA10_0 <= '\uFFFD')) ) {
				alt10=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}

			switch (alt10) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1246:19: STRING_MACR
					{
					mSTRING_MACR(); 

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1246:33: URI_MACR
					{
					mURI_MACR(); 

					}
					break;

			}

			mW_MACR(); 

			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "URI"

	// $ANTLR start "UNIRANGE"
	public final void mUNIRANGE() throws RecognitionException {
		try {
			int _type = UNIRANGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1250:9: ( 'U+' ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) )? ( '-' ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )? )? )
			// cz/vutbr/web/csskit/antlr/CSS.g:1251:2: 'U+' ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) )? ( '-' ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )? )?
			{
			match("U+"); 

			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||input.LA(1)=='?'||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||input.LA(1)=='?'||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||input.LA(1)=='?'||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||input.LA(1)=='?'||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// cz/vutbr/web/csskit/antlr/CSS.g:1255:7: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( ((LA11_0 >= '0' && LA11_0 <= '9')||LA11_0=='?'||(LA11_0 >= 'A' && LA11_0 <= 'F')||(LA11_0 >= 'a' && LA11_0 <= 'f')) ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1255:8: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '?' )
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||input.LA(1)=='?'||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||input.LA(1)=='?'||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			// cz/vutbr/web/csskit/antlr/CSS.g:1256:2: ( '-' ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )? )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0=='-') ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1256:3: '-' ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )?
					{
					match('-'); 
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					// cz/vutbr/web/csskit/antlr/CSS.g:1261:14: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( ((LA12_0 >= '0' && LA12_0 <= '9')||(LA12_0 >= 'A' && LA12_0 <= 'F')||(LA12_0 >= 'a' && LA12_0 <= 'f')) ) {
						alt12=1;
					}
					switch (alt12) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1261:15: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

					}

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UNIRANGE"

	// $ANTLR start "CDO"
	public final void mCDO() throws RecognitionException {
		try {
			int _type = CDO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1267:2: ( '<!--' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1267:4: '<!--'
			{
			match("<!--"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CDO"

	// $ANTLR start "CDC"
	public final void mCDC() throws RecognitionException {
		try {
			int _type = CDC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1272:2: ( '-->' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1272:4: '-->'
			{
			match("-->"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CDC"

	// $ANTLR start "SEMICOLON"
	public final void mSEMICOLON() throws RecognitionException {
		try {
			int _type = SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1275:2: ( ';' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1275:4: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SEMICOLON"

	// $ANTLR start "COLON"
	public final void mCOLON() throws RecognitionException {
		try {
			int _type = COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1279:2: ( ':' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1279:4: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COLON"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1283:5: ( ',' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1283:7: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "QUESTION"
	public final void mQUESTION() throws RecognitionException {
		try {
			int _type = QUESTION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1287:2: ( '?' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1287:4: '?'
			{
			match('?'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QUESTION"

	// $ANTLR start "PERCENT"
	public final void mPERCENT() throws RecognitionException {
		try {
			int _type = PERCENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1291:2: ( '%' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1291:4: '%'
			{
			match('%'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PERCENT"

	// $ANTLR start "EQUALS"
	public final void mEQUALS() throws RecognitionException {
		try {
			int _type = EQUALS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1295:5: ( '=' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1295:7: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EQUALS"

	// $ANTLR start "SLASH"
	public final void mSLASH() throws RecognitionException {
		try {
			int _type = SLASH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1299:5: ( '/' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1299:7: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SLASH"

	// $ANTLR start "GREATER"
	public final void mGREATER() throws RecognitionException {
		try {
			int _type = GREATER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1303:5: ( '>' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1303:7: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GREATER"

	// $ANTLR start "LESS"
	public final void mLESS() throws RecognitionException {
		try {
			int _type = LESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1307:5: ( '<' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1307:7: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LESS"

	// $ANTLR start "LCURLY"
	public final void mLCURLY() throws RecognitionException {
		try {
			int _type = LCURLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1311:2: ( '{' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1311:4: '{'
			{
			match('{'); 
			ls.curlyNest++;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LCURLY"

	// $ANTLR start "RCURLY"
	public final void mRCURLY() throws RecognitionException {
		try {
			int _type = RCURLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1315:2: ( '}' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1315:4: '}'
			{
			match('}'); 
			 if(ls.curlyNest>0) ls.curlyNest--;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RCURLY"

	// $ANTLR start "APOS"
	public final void mAPOS() throws RecognitionException {
		try {
			int _type = APOS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1319:2: ( '\\'' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1319:4: '\\''
			{
			match('\''); 
			 ls.aposOpen=!ls.aposOpen; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "APOS"

	// $ANTLR start "QUOT"
	public final void mQUOT() throws RecognitionException {
		try {
			int _type = QUOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1323:2: ( '\"' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1323:4: '\"'
			{
			match('\"'); 
			 ls.quotOpen=!ls.quotOpen; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QUOT"

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1327:2: ( '(' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1327:4: '('
			{
			match('('); 
			ls.parenNest++; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LPAREN"

	// $ANTLR start "RPAREN"
	public final void mRPAREN() throws RecognitionException {
		try {
			int _type = RPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1331:2: ( ')' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1331:4: ')'
			{
			match(')'); 
			 if(ls.parenNest>0) ls.parenNest--; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RPAREN"

	// $ANTLR start "LBRACE"
	public final void mLBRACE() throws RecognitionException {
		try {
			int _type = LBRACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1335:2: ( '[' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1335:4: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LBRACE"

	// $ANTLR start "RBRACE"
	public final void mRBRACE() throws RecognitionException {
		try {
			int _type = RBRACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1339:2: ( ']' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1339:4: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RBRACE"

	// $ANTLR start "EXCLAMATION"
	public final void mEXCLAMATION() throws RecognitionException {
		try {
			int _type = EXCLAMATION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1343:5: ( '!' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1343:7: '!'
			{
			match('!'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EXCLAMATION"

	// $ANTLR start "TILDE"
	public final void mTILDE() throws RecognitionException {
		try {
			int _type = TILDE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1347:3: ( '~' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1347:5: '~'
			{
			match('~'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TILDE"

	// $ANTLR start "MINUS"
	public final void mMINUS() throws RecognitionException {
		try {
			int _type = MINUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1351:2: ( '-' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1351:4: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MINUS"

	// $ANTLR start "PLUS"
	public final void mPLUS() throws RecognitionException {
		try {
			int _type = PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1355:2: ( '+' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1355:4: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUS"

	// $ANTLR start "ASTERISK"
	public final void mASTERISK() throws RecognitionException {
		try {
			int _type = ASTERISK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1359:2: ( '*' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1359:4: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASTERISK"

	// $ANTLR start "S"
	public final void mS() throws RecognitionException {
		try {
			int _type = S;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1365:2: ( ( W_CHAR )+ )
			// cz/vutbr/web/csskit/antlr/CSS.g:1365:4: ( W_CHAR )+
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1365:4: ( W_CHAR )+
			int cnt14=0;
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( ((LA14_0 >= '\t' && LA14_0 <= '\r')||LA14_0==' ') ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt14 >= 1 ) break loop14;
					EarlyExitException eee = new EarlyExitException(14, input);
					throw eee;
				}
				cnt14++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "S"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1368:2: ( '/*' ( options {greedy=false; } : . )* '*/' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1368:4: '/*' ( options {greedy=false; } : . )* '*/'
			{
			match("/*"); 

			// cz/vutbr/web/csskit/antlr/CSS.g:1368:9: ( options {greedy=false; } : . )*
			loop15:
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( (LA15_0=='*') ) {
					int LA15_1 = input.LA(2);
					if ( (LA15_1=='/') ) {
						alt15=2;
					}
					else if ( ((LA15_1 >= '\u0000' && LA15_1 <= '.')||(LA15_1 >= '0' && LA15_1 <= '\uFFFF')) ) {
						alt15=1;
					}

				}
				else if ( ((LA15_0 >= '\u0000' && LA15_0 <= ')')||(LA15_0 >= '+' && LA15_0 <= '\uFFFF')) ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1368:37: .
					{
					matchAny(); 
					}
					break;

				default :
					break loop15;
				}
			}

			match("*/"); 

			 _channel = HIDDEN; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "SL_COMMENT"
	public final void mSL_COMMENT() throws RecognitionException {
		try {
			int _type = SL_COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1372:2: ( '//' ( options {greedy=false; } : . )* ( '\\n' | '\\r' ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:1372:4: '//' ( options {greedy=false; } : . )* ( '\\n' | '\\r' )
			{
			match("//"); 

			// cz/vutbr/web/csskit/antlr/CSS.g:1372:9: ( options {greedy=false; } : . )*
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( (LA16_0=='\n'||LA16_0=='\r') ) {
					alt16=2;
				}
				else if ( ((LA16_0 >= '\u0000' && LA16_0 <= '\t')||(LA16_0 >= '\u000B' && LA16_0 <= '\f')||(LA16_0 >= '\u000E' && LA16_0 <= '\uFFFF')) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1372:37: .
					{
					matchAny(); 
					}
					break;

				default :
					break loop16;
				}
			}

			if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			 _channel=HIDDEN; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SL_COMMENT"

	// $ANTLR start "EXPRESSION"
	public final void mEXPRESSION() throws RecognitionException {
		try {
			int _type = EXPRESSION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1378:3: ( 'expression(' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1378:5: 'expression('
			{
			match("expression("); 

			 readExpressionContents(); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EXPRESSION"

	// $ANTLR start "FUNCTION"
	public final void mFUNCTION() throws RecognitionException {
		try {
			int _type = FUNCTION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1383:2: ( IDENT_MACR '(' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1383:4: IDENT_MACR '('
			{
			mIDENT_MACR(); 

			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FUNCTION"

	// $ANTLR start "INCLUDES"
	public final void mINCLUDES() throws RecognitionException {
		try {
			int _type = INCLUDES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1386:2: ( '~=' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1386:4: '~='
			{
			match("~="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INCLUDES"

	// $ANTLR start "DASHMATCH"
	public final void mDASHMATCH() throws RecognitionException {
		try {
			int _type = DASHMATCH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1390:2: ( '|=' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1390:4: '|='
			{
			match("|="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DASHMATCH"

	// $ANTLR start "STARTSWITH"
	public final void mSTARTSWITH() throws RecognitionException {
		try {
			int _type = STARTSWITH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1394:3: ( '^=' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1394:5: '^='
			{
			match("^="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STARTSWITH"

	// $ANTLR start "ENDSWITH"
	public final void mENDSWITH() throws RecognitionException {
		try {
			int _type = ENDSWITH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1398:3: ( '$=' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1398:5: '$='
			{
			match("$="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ENDSWITH"

	// $ANTLR start "CONTAINS"
	public final void mCONTAINS() throws RecognitionException {
		try {
			int _type = CONTAINS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1402:3: ( '*=' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1402:5: '*='
			{
			match("*="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CONTAINS"

	// $ANTLR start "CTRL"
	public final void mCTRL() throws RecognitionException {
		try {
			int _type = CTRL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1406:3: ( ( CTRL_CHAR )+ )
			// cz/vutbr/web/csskit/antlr/CSS.g:1406:5: ( CTRL_CHAR )+
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1406:5: ( CTRL_CHAR )+
			int cnt17=0;
			loop17:
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( ((LA17_0 >= '\u0000' && LA17_0 <= '\b')||(LA17_0 >= '\u000E' && LA17_0 <= '\u001F')) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt17 >= 1 ) break loop17;
					EarlyExitException eee = new EarlyExitException(17, input);
					throw eee;
				}
				cnt17++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CTRL"

	// $ANTLR start "INVALID_TOKEN"
	public final void mINVALID_TOKEN() throws RecognitionException {
		try {
			int _type = INVALID_TOKEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// cz/vutbr/web/csskit/antlr/CSS.g:1410:2: ( . )
			// cz/vutbr/web/csskit/antlr/CSS.g:1410:4: .
			{
			matchAny(); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INVALID_TOKEN"

	// $ANTLR start "IDENT_MACR"
	public final void mIDENT_MACR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1424:4: ( NAME_START ( NAME_CHAR )* )
			// cz/vutbr/web/csskit/antlr/CSS.g:1424:6: NAME_START ( NAME_CHAR )*
			{
			mNAME_START(); 

			// cz/vutbr/web/csskit/antlr/CSS.g:1424:17: ( NAME_CHAR )*
			loop18:
			while (true) {
				int alt18=2;
				int LA18_0 = input.LA(1);
				if ( (LA18_0=='-'||(LA18_0 >= '0' && LA18_0 <= '9')||(LA18_0 >= 'A' && LA18_0 <= 'Z')||LA18_0=='\\'||LA18_0=='_'||(LA18_0 >= 'a' && LA18_0 <= 'z')||(LA18_0 >= '\u0080' && LA18_0 <= '\uD7FF')||(LA18_0 >= '\uE000' && LA18_0 <= '\uFFFD')) ) {
					alt18=1;
				}

				switch (alt18) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1424:17: NAME_CHAR
					{
					mNAME_CHAR(); 

					}
					break;

				default :
					break loop18;
				}
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IDENT_MACR"

	// $ANTLR start "NAME_MACR"
	public final void mNAME_MACR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1425:3: ( ( NAME_CHAR )+ )
			// cz/vutbr/web/csskit/antlr/CSS.g:1425:5: ( NAME_CHAR )+
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1425:5: ( NAME_CHAR )+
			int cnt19=0;
			loop19:
			while (true) {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( (LA19_0=='-'||(LA19_0 >= '0' && LA19_0 <= '9')||(LA19_0 >= 'A' && LA19_0 <= 'Z')||LA19_0=='\\'||LA19_0=='_'||(LA19_0 >= 'a' && LA19_0 <= 'z')||(LA19_0 >= '\u0080' && LA19_0 <= '\uD7FF')||(LA19_0 >= '\uE000' && LA19_0 <= '\uFFFD')) ) {
					alt19=1;
				}

				switch (alt19) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1425:5: NAME_CHAR
					{
					mNAME_CHAR(); 

					}
					break;

				default :
					if ( cnt19 >= 1 ) break loop19;
					EarlyExitException eee = new EarlyExitException(19, input);
					throw eee;
				}
				cnt19++;
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NAME_MACR"

	// $ANTLR start "NAME_START"
	public final void mNAME_START() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1430:4: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | NON_ASCII | ESCAPE_CHAR ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:1430:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | NON_ASCII | ESCAPE_CHAR )
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1430:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | NON_ASCII | ESCAPE_CHAR )
			int alt20=5;
			int LA20_0 = input.LA(1);
			if ( ((LA20_0 >= 'a' && LA20_0 <= 'z')) ) {
				alt20=1;
			}
			else if ( ((LA20_0 >= 'A' && LA20_0 <= 'Z')) ) {
				alt20=2;
			}
			else if ( (LA20_0=='_') ) {
				alt20=3;
			}
			else if ( ((LA20_0 >= '\u0080' && LA20_0 <= '\uD7FF')||(LA20_0 >= '\uE000' && LA20_0 <= '\uFFFD')) ) {
				alt20=4;
			}
			else if ( (LA20_0=='\\') ) {
				alt20=5;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 20, 0, input);
				throw nvae;
			}

			switch (alt20) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1430:7: 'a' .. 'z'
					{
					matchRange('a','z'); 
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1430:18: 'A' .. 'Z'
					{
					matchRange('A','Z'); 
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1430:29: '_'
					{
					match('_'); 
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1430:35: NON_ASCII
					{
					mNON_ASCII(); 

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1430:47: ESCAPE_CHAR
					{
					mESCAPE_CHAR(); 

					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NAME_START"

	// $ANTLR start "NON_ASCII"
	public final void mNON_ASCII() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1435:4: ( ( '\\u0080' .. '\\uD7FF' | '\\uE000' .. '\\uFFFD' ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:
			{
			if ( (input.LA(1) >= '\u0080' && input.LA(1) <= '\uD7FF')||(input.LA(1) >= '\uE000' && input.LA(1) <= '\uFFFD') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NON_ASCII"

	// $ANTLR start "ESCAPE_CHAR"
	public final void mESCAPE_CHAR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1440:3: ( ( '\\\\' ) ( ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )? ) | ( '\\u0020' .. '\\u007E' | '\\u0080' .. '\\uD7FF' | '\\uE000' .. '\\uFFFD' ) ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:1440:5: ( '\\\\' ) ( ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )? ) | ( '\\u0020' .. '\\u007E' | '\\u0080' .. '\\uD7FF' | '\\uE000' .. '\\uFFFD' ) )
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1440:5: ( '\\\\' )
			// cz/vutbr/web/csskit/antlr/CSS.g:1440:6: '\\\\'
			{
			match('\\'); 
			}

			// cz/vutbr/web/csskit/antlr/CSS.g:1441:5: ( ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )? ) | ( '\\u0020' .. '\\u007E' | '\\u0080' .. '\\uD7FF' | '\\uE000' .. '\\uFFFD' ) )
			int alt22=2;
			int LA22_0 = input.LA(1);
			if ( ((LA22_0 >= '0' && LA22_0 <= '9')||(LA22_0 >= 'A' && LA22_0 <= 'F')||(LA22_0 >= 'a' && LA22_0 <= 'f')) ) {
				int LA22_1 = input.LA(2);
				if ( ((LA22_1 >= '0' && LA22_1 <= '9')||(LA22_1 >= 'A' && LA22_1 <= 'F')||(LA22_1 >= 'a' && LA22_1 <= 'f')) ) {
					alt22=1;
				}

				else {
					alt22=2;
				}

			}
			else if ( ((LA22_0 >= ' ' && LA22_0 <= '/')||(LA22_0 >= ':' && LA22_0 <= '@')||(LA22_0 >= 'G' && LA22_0 <= '`')||(LA22_0 >= 'g' && LA22_0 <= '~')||(LA22_0 >= '\u0080' && LA22_0 <= '\uD7FF')||(LA22_0 >= '\uE000' && LA22_0 <= '\uFFFD')) ) {
				alt22=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 22, 0, input);
				throw nvae;
			}

			switch (alt22) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1442:7: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )? )
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:1442:7: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )? )
					// cz/vutbr/web/csskit/antlr/CSS.g:1442:8: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )?
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					// cz/vutbr/web/csskit/antlr/CSS.g:1446:8: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( ((LA21_0 >= '0' && LA21_0 <= '9')||(LA21_0 >= 'A' && LA21_0 <= 'F')||(LA21_0 >= 'a' && LA21_0 <= 'f')) ) {
						alt21=1;
					}
					switch (alt21) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1446:9: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

					}

					}

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1449:7: ( '\\u0020' .. '\\u007E' | '\\u0080' .. '\\uD7FF' | '\\uE000' .. '\\uFFFD' )
					{
					if ( (input.LA(1) >= ' ' && input.LA(1) <= '~')||(input.LA(1) >= '\u0080' && input.LA(1) <= '\uD7FF')||(input.LA(1) >= '\uE000' && input.LA(1) <= '\uFFFD') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ESCAPE_CHAR"

	// $ANTLR start "NAME_CHAR"
	public final void mNAME_CHAR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1455:4: ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '-' | '_' | NON_ASCII | ESCAPE_CHAR ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:1455:6: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '-' | '_' | NON_ASCII | ESCAPE_CHAR )
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1455:6: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '-' | '_' | NON_ASCII | ESCAPE_CHAR )
			int alt23=7;
			int LA23_0 = input.LA(1);
			if ( ((LA23_0 >= 'a' && LA23_0 <= 'z')) ) {
				alt23=1;
			}
			else if ( ((LA23_0 >= 'A' && LA23_0 <= 'Z')) ) {
				alt23=2;
			}
			else if ( ((LA23_0 >= '0' && LA23_0 <= '9')) ) {
				alt23=3;
			}
			else if ( (LA23_0=='-') ) {
				alt23=4;
			}
			else if ( (LA23_0=='_') ) {
				alt23=5;
			}
			else if ( ((LA23_0 >= '\u0080' && LA23_0 <= '\uD7FF')||(LA23_0 >= '\uE000' && LA23_0 <= '\uFFFD')) ) {
				alt23=6;
			}
			else if ( (LA23_0=='\\') ) {
				alt23=7;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 23, 0, input);
				throw nvae;
			}

			switch (alt23) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1455:7: 'a' .. 'z'
					{
					matchRange('a','z'); 
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1455:18: 'A' .. 'Z'
					{
					matchRange('A','Z'); 
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1455:29: '0' .. '9'
					{
					matchRange('0','9'); 
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1455:40: '-'
					{
					match('-'); 
					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1455:46: '_'
					{
					match('_'); 
					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1455:52: NON_ASCII
					{
					mNON_ASCII(); 

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1455:64: ESCAPE_CHAR
					{
					mESCAPE_CHAR(); 

					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NAME_CHAR"

	// $ANTLR start "INTEGER_MACR"
	public final void mINTEGER_MACR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1460:5: ( ( '0' .. '9' )+ )
			// cz/vutbr/web/csskit/antlr/CSS.g:1460:7: ( '0' .. '9' )+
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1460:7: ( '0' .. '9' )+
			int cnt24=0;
			loop24:
			while (true) {
				int alt24=2;
				int LA24_0 = input.LA(1);
				if ( ((LA24_0 >= '0' && LA24_0 <= '9')) ) {
					alt24=1;
				}

				switch (alt24) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt24 >= 1 ) break loop24;
					EarlyExitException eee = new EarlyExitException(24, input);
					throw eee;
				}
				cnt24++;
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INTEGER_MACR"

	// $ANTLR start "NUMBER_MACR"
	public final void mNUMBER_MACR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1465:4: ( ( '0' .. '9' )+ | ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ ) )
			int alt28=2;
			alt28 = dfa28.predict(input);
			switch (alt28) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1465:6: ( '0' .. '9' )+
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:1465:6: ( '0' .. '9' )+
					int cnt25=0;
					loop25:
					while (true) {
						int alt25=2;
						int LA25_0 = input.LA(1);
						if ( ((LA25_0 >= '0' && LA25_0 <= '9')) ) {
							alt25=1;
						}

						switch (alt25) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt25 >= 1 ) break loop25;
							EarlyExitException eee = new EarlyExitException(25, input);
							throw eee;
						}
						cnt25++;
					}

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1465:20: ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ )
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:1465:20: ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ )
					// cz/vutbr/web/csskit/antlr/CSS.g:1465:21: ( '0' .. '9' )* '.' ( '0' .. '9' )+
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:1465:21: ( '0' .. '9' )*
					loop26:
					while (true) {
						int alt26=2;
						int LA26_0 = input.LA(1);
						if ( ((LA26_0 >= '0' && LA26_0 <= '9')) ) {
							alt26=1;
						}

						switch (alt26) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop26;
						}
					}

					match('.'); 
					// cz/vutbr/web/csskit/antlr/CSS.g:1465:37: ( '0' .. '9' )+
					int cnt27=0;
					loop27:
					while (true) {
						int alt27=2;
						int LA27_0 = input.LA(1);
						if ( ((LA27_0 >= '0' && LA27_0 <= '9')) ) {
							alt27=1;
						}

						switch (alt27) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt27 >= 1 ) break loop27;
							EarlyExitException eee = new EarlyExitException(27, input);
							throw eee;
						}
						cnt27++;
					}

					}

					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMBER_MACR"

	// $ANTLR start "STRING_MACR"
	public final void mSTRING_MACR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1470:2: ( QUOT ( STRING_CHAR | APOS )* QUOT | APOS ( STRING_CHAR | QUOT )* APOS )
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0=='\"') ) {
				alt31=1;
			}
			else if ( (LA31_0=='\'') ) {
				alt31=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 31, 0, input);
				throw nvae;
			}

			switch (alt31) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1470:4: QUOT ( STRING_CHAR | APOS )* QUOT
					{
					mQUOT(); 

					// cz/vutbr/web/csskit/antlr/CSS.g:1470:9: ( STRING_CHAR | APOS )*
					loop29:
					while (true) {
						int alt29=3;
						int LA29_0 = input.LA(1);
						if ( (LA29_0=='\t'||(LA29_0 >= ' ' && LA29_0 <= '!')||(LA29_0 >= '#' && LA29_0 <= '&')||(LA29_0 >= '(' && LA29_0 <= '~')||(LA29_0 >= '\u0080' && LA29_0 <= '\uD7FF')||(LA29_0 >= '\uE000' && LA29_0 <= '\uFFFD')) ) {
							alt29=1;
						}
						else if ( (LA29_0=='\'') ) {
							alt29=2;
						}

						switch (alt29) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1470:10: STRING_CHAR
							{
							mSTRING_CHAR(); 

							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1470:24: APOS
							{
							mAPOS(); 

							ls.aposOpen=false;
							}
							break;

						default :
							break loop29;
						}
					}

					mQUOT(); 

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1471:4: APOS ( STRING_CHAR | QUOT )* APOS
					{
					mAPOS(); 

					// cz/vutbr/web/csskit/antlr/CSS.g:1471:9: ( STRING_CHAR | QUOT )*
					loop30:
					while (true) {
						int alt30=3;
						int LA30_0 = input.LA(1);
						if ( (LA30_0=='\t'||(LA30_0 >= ' ' && LA30_0 <= '!')||(LA30_0 >= '#' && LA30_0 <= '&')||(LA30_0 >= '(' && LA30_0 <= '~')||(LA30_0 >= '\u0080' && LA30_0 <= '\uD7FF')||(LA30_0 >= '\uE000' && LA30_0 <= '\uFFFD')) ) {
							alt30=1;
						}
						else if ( (LA30_0=='\"') ) {
							alt30=2;
						}

						switch (alt30) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1471:10: STRING_CHAR
							{
							mSTRING_CHAR(); 

							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1471:24: QUOT
							{
							mQUOT(); 

							ls.quotOpen=false;
							}
							break;

						default :
							break loop30;
						}
					}

					mAPOS(); 

					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING_MACR"

	// $ANTLR start "STRING_CHAR"
	public final void mSTRING_CHAR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1476:2: ( ( URI_CHAR | ' ' | '(' | ')' | ( '\\\\' NL_CHAR ) ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:1476:5: ( URI_CHAR | ' ' | '(' | ')' | ( '\\\\' NL_CHAR ) )
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1476:5: ( URI_CHAR | ' ' | '(' | ')' | ( '\\\\' NL_CHAR ) )
			int alt32=5;
			int LA32_0 = input.LA(1);
			if ( (LA32_0=='\\') ) {
				int LA32_1 = input.LA(2);
				if ( (LA32_1=='\n'||(LA32_1 >= '\f' && LA32_1 <= '\r')) ) {
					alt32=5;
				}

				else {
					alt32=1;
				}

			}
			else if ( (LA32_0=='\t'||LA32_0=='!'||(LA32_0 >= '#' && LA32_0 <= '&')||(LA32_0 >= '*' && LA32_0 <= '[')||(LA32_0 >= ']' && LA32_0 <= '~')||(LA32_0 >= '\u0080' && LA32_0 <= '\uD7FF')||(LA32_0 >= '\uE000' && LA32_0 <= '\uFFFD')) ) {
				alt32=1;
			}
			else if ( (LA32_0==' ') ) {
				alt32=2;
			}
			else if ( (LA32_0=='(') ) {
				alt32=3;
			}
			else if ( (LA32_0==')') ) {
				alt32=4;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 32, 0, input);
				throw nvae;
			}

			switch (alt32) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1476:6: URI_CHAR
					{
					mURI_CHAR(); 

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1476:17: ' '
					{
					match(' '); 
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1476:23: '('
					{
					match('('); 
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1476:29: ')'
					{
					match(')'); 
					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1476:35: ( '\\\\' NL_CHAR )
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:1476:35: ( '\\\\' NL_CHAR )
					// cz/vutbr/web/csskit/antlr/CSS.g:1476:36: '\\\\' NL_CHAR
					{
					match('\\'); 
					mNL_CHAR(); 

					}

					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING_CHAR"

	// $ANTLR start "URI_MACR"
	public final void mURI_MACR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1481:2: ( ( URI_CHAR )* )
			// cz/vutbr/web/csskit/antlr/CSS.g:1481:4: ( URI_CHAR )*
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1481:4: ( URI_CHAR )*
			loop33:
			while (true) {
				int alt33=2;
				int LA33_0 = input.LA(1);
				if ( (LA33_0=='\t'||LA33_0=='!'||(LA33_0 >= '#' && LA33_0 <= '&')||(LA33_0 >= '*' && LA33_0 <= '~')||(LA33_0 >= '\u0080' && LA33_0 <= '\uD7FF')||(LA33_0 >= '\uE000' && LA33_0 <= '\uFFFD')) ) {
					alt33=1;
				}

				switch (alt33) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1481:4: URI_CHAR
					{
					mURI_CHAR(); 

					}
					break;

				default :
					break loop33;
				}
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "URI_MACR"

	// $ANTLR start "URI_CHAR"
	public final void mURI_CHAR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1486:2: ( ( '\\u0009' | '\\u0021' | '\\u0023' .. '\\u0026' | '\\u002A' .. '\\u007E' ) | NON_ASCII | ESCAPE_CHAR )
			int alt34=3;
			int LA34_0 = input.LA(1);
			if ( (LA34_0=='\\') ) {
				int LA34_1 = input.LA(2);
				if ( ((LA34_1 >= ' ' && LA34_1 <= '~')||(LA34_1 >= '\u0080' && LA34_1 <= '\uD7FF')||(LA34_1 >= '\uE000' && LA34_1 <= '\uFFFD')) ) {
					alt34=3;
				}

				else {
					alt34=1;
				}

			}
			else if ( ((LA34_0 >= '\u0080' && LA34_0 <= '\uD7FF')||(LA34_0 >= '\uE000' && LA34_0 <= '\uFFFD')) ) {
				alt34=2;
			}
			else if ( (LA34_0=='\t'||LA34_0=='!'||(LA34_0 >= '#' && LA34_0 <= '&')||(LA34_0 >= '*' && LA34_0 <= '[')||(LA34_0 >= ']' && LA34_0 <= '~')) ) {
				alt34=1;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 34, 0, input);
				throw nvae;
			}

			switch (alt34) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1486:4: ( '\\u0009' | '\\u0021' | '\\u0023' .. '\\u0026' | '\\u002A' .. '\\u007E' )
					{
					if ( input.LA(1)=='\t'||input.LA(1)=='!'||(input.LA(1) >= '#' && input.LA(1) <= '&')||(input.LA(1) >= '*' && input.LA(1) <= '~') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1487:6: NON_ASCII
					{
					mNON_ASCII(); 

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1487:18: ESCAPE_CHAR
					{
					mESCAPE_CHAR(); 

					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "URI_CHAR"

	// $ANTLR start "NL_CHAR"
	public final void mNL_CHAR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1492:4: ( '\\u000A' | '\\u000D' '\\u000A' | '\\u000D' | '\\u000C' )
			int alt35=4;
			switch ( input.LA(1) ) {
			case '\n':
				{
				alt35=1;
				}
				break;
			case '\r':
				{
				int LA35_2 = input.LA(2);
				if ( (LA35_2=='\n') ) {
					alt35=2;
				}

				else {
					alt35=3;
				}

				}
				break;
			case '\f':
				{
				alt35=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 35, 0, input);
				throw nvae;
			}
			switch (alt35) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1492:6: '\\u000A'
					{
					match('\n'); 
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1492:17: '\\u000D' '\\u000A'
					{
					match('\r'); 
					match('\n'); 
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1492:37: '\\u000D'
					{
					match('\r'); 
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1492:48: '\\u000C'
					{
					match('\f'); 
					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NL_CHAR"

	// $ANTLR start "W_MACR"
	public final void mW_MACR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1497:2: ( ( W_CHAR )* )
			// cz/vutbr/web/csskit/antlr/CSS.g:1497:4: ( W_CHAR )*
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1497:4: ( W_CHAR )*
			loop36:
			while (true) {
				int alt36=2;
				int LA36_0 = input.LA(1);
				if ( ((LA36_0 >= '\t' && LA36_0 <= '\r')||LA36_0==' ') ) {
					alt36=1;
				}

				switch (alt36) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop36;
				}
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "W_MACR"

	// $ANTLR start "W_CHAR"
	public final void mW_CHAR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1502:4: ( '\\u0009' | '\\u000A' | '\\u000B' | '\\u000C' | '\\u000D' | '\\u0020' )
			// cz/vutbr/web/csskit/antlr/CSS.g:
			{
			if ( (input.LA(1) >= '\t' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "W_CHAR"

	// $ANTLR start "CTRL_CHAR"
	public final void mCTRL_CHAR() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1507:5: ( '\\u0000' .. '\\u0008' | '\\u000E' .. '\\u001F' )
			// cz/vutbr/web/csskit/antlr/CSS.g:
			{
			if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CTRL_CHAR"

	@Override
	public void mTokens() throws RecognitionException {
		// cz/vutbr/web/csskit/antlr/CSS.g:1:8: ( T__101 | T__102 | T__103 | T__104 | IDENT | CHARSET | IMPORT | MEDIA | PAGE | MARGIN_AREA | VIEWPORT | FONTFACE | ATKEYWORD | CLASSKEYWORD | STRING | HASH | INDEX | NUMBER | PERCENTAGE | DIMENSION | URI | UNIRANGE | CDO | CDC | SEMICOLON | COLON | COMMA | QUESTION | PERCENT | EQUALS | SLASH | GREATER | LESS | LCURLY | RCURLY | APOS | QUOT | LPAREN | RPAREN | LBRACE | RBRACE | EXCLAMATION | TILDE | MINUS | PLUS | ASTERISK | S | COMMENT | SL_COMMENT | EXPRESSION | FUNCTION | INCLUDES | DASHMATCH | STARTSWITH | ENDSWITH | CONTAINS | CTRL | INVALID_TOKEN )
		int alt37=58;
		alt37 = dfa37.predict(input);
		switch (alt37) {
			case 1 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:10: T__101
				{
				mT__101(); 

				}
				break;
			case 2 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:17: T__102
				{
				mT__102(); 

				}
				break;
			case 3 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:24: T__103
				{
				mT__103(); 

				}
				break;
			case 4 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:31: T__104
				{
				mT__104(); 

				}
				break;
			case 5 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:38: IDENT
				{
				mIDENT(); 

				}
				break;
			case 6 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:44: CHARSET
				{
				mCHARSET(); 

				}
				break;
			case 7 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:52: IMPORT
				{
				mIMPORT(); 

				}
				break;
			case 8 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:59: MEDIA
				{
				mMEDIA(); 

				}
				break;
			case 9 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:65: PAGE
				{
				mPAGE(); 

				}
				break;
			case 10 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:70: MARGIN_AREA
				{
				mMARGIN_AREA(); 

				}
				break;
			case 11 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:82: VIEWPORT
				{
				mVIEWPORT(); 

				}
				break;
			case 12 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:91: FONTFACE
				{
				mFONTFACE(); 

				}
				break;
			case 13 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:100: ATKEYWORD
				{
				mATKEYWORD(); 

				}
				break;
			case 14 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:110: CLASSKEYWORD
				{
				mCLASSKEYWORD(); 

				}
				break;
			case 15 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:123: STRING
				{
				mSTRING(); 

				}
				break;
			case 16 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:130: HASH
				{
				mHASH(); 

				}
				break;
			case 17 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:135: INDEX
				{
				mINDEX(); 

				}
				break;
			case 18 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:141: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 19 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:148: PERCENTAGE
				{
				mPERCENTAGE(); 

				}
				break;
			case 20 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:159: DIMENSION
				{
				mDIMENSION(); 

				}
				break;
			case 21 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:169: URI
				{
				mURI(); 

				}
				break;
			case 22 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:173: UNIRANGE
				{
				mUNIRANGE(); 

				}
				break;
			case 23 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:182: CDO
				{
				mCDO(); 

				}
				break;
			case 24 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:186: CDC
				{
				mCDC(); 

				}
				break;
			case 25 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:190: SEMICOLON
				{
				mSEMICOLON(); 

				}
				break;
			case 26 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:200: COLON
				{
				mCOLON(); 

				}
				break;
			case 27 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:206: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 28 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:212: QUESTION
				{
				mQUESTION(); 

				}
				break;
			case 29 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:221: PERCENT
				{
				mPERCENT(); 

				}
				break;
			case 30 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:229: EQUALS
				{
				mEQUALS(); 

				}
				break;
			case 31 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:236: SLASH
				{
				mSLASH(); 

				}
				break;
			case 32 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:242: GREATER
				{
				mGREATER(); 

				}
				break;
			case 33 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:250: LESS
				{
				mLESS(); 

				}
				break;
			case 34 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:255: LCURLY
				{
				mLCURLY(); 

				}
				break;
			case 35 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:262: RCURLY
				{
				mRCURLY(); 

				}
				break;
			case 36 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:269: APOS
				{
				mAPOS(); 

				}
				break;
			case 37 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:274: QUOT
				{
				mQUOT(); 

				}
				break;
			case 38 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:279: LPAREN
				{
				mLPAREN(); 

				}
				break;
			case 39 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:286: RPAREN
				{
				mRPAREN(); 

				}
				break;
			case 40 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:293: LBRACE
				{
				mLBRACE(); 

				}
				break;
			case 41 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:300: RBRACE
				{
				mRBRACE(); 

				}
				break;
			case 42 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:307: EXCLAMATION
				{
				mEXCLAMATION(); 

				}
				break;
			case 43 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:319: TILDE
				{
				mTILDE(); 

				}
				break;
			case 44 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:325: MINUS
				{
				mMINUS(); 

				}
				break;
			case 45 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:331: PLUS
				{
				mPLUS(); 

				}
				break;
			case 46 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:336: ASTERISK
				{
				mASTERISK(); 

				}
				break;
			case 47 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:345: S
				{
				mS(); 

				}
				break;
			case 48 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:347: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 49 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:355: SL_COMMENT
				{
				mSL_COMMENT(); 

				}
				break;
			case 50 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:366: EXPRESSION
				{
				mEXPRESSION(); 

				}
				break;
			case 51 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:377: FUNCTION
				{
				mFUNCTION(); 

				}
				break;
			case 52 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:386: INCLUDES
				{
				mINCLUDES(); 

				}
				break;
			case 53 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:395: DASHMATCH
				{
				mDASHMATCH(); 

				}
				break;
			case 54 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:405: STARTSWITH
				{
				mSTARTSWITH(); 

				}
				break;
			case 55 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:416: ENDSWITH
				{
				mENDSWITH(); 

				}
				break;
			case 56 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:425: CONTAINS
				{
				mCONTAINS(); 

				}
				break;
			case 57 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:434: CTRL
				{
				mCTRL(); 

				}
				break;
			case 58 :
				// cz/vutbr/web/csskit/antlr/CSS.g:1:439: INVALID_TOKEN
				{
				mINVALID_TOKEN(); 

				}
				break;

		}
	}


	protected DFA28 dfa28 = new DFA28(this);
	protected DFA37 dfa37 = new DFA37(this);
	static final String DFA28_eotS =
		"\1\uffff\1\3\2\uffff";
	static final String DFA28_eofS =
		"\4\uffff";
	static final String DFA28_minS =
		"\2\56\2\uffff";
	static final String DFA28_maxS =
		"\2\71\2\uffff";
	static final String DFA28_acceptS =
		"\2\uffff\1\2\1\1";
	static final String DFA28_specialS =
		"\4\uffff}>";
	static final String[] DFA28_transitionS = {
			"\1\2\1\uffff\12\1",
			"\1\2\1\uffff\12\1",
			"",
			""
	};

	static final short[] DFA28_eot = DFA.unpackEncodedString(DFA28_eotS);
	static final short[] DFA28_eof = DFA.unpackEncodedString(DFA28_eofS);
	static final char[] DFA28_min = DFA.unpackEncodedStringToUnsignedChars(DFA28_minS);
	static final char[] DFA28_max = DFA.unpackEncodedStringToUnsignedChars(DFA28_maxS);
	static final short[] DFA28_accept = DFA.unpackEncodedString(DFA28_acceptS);
	static final short[] DFA28_special = DFA.unpackEncodedString(DFA28_specialS);
	static final short[][] DFA28_transition;

	static {
		int numStates = DFA28_transitionS.length;
		DFA28_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA28_transition[i] = DFA.unpackEncodedString(DFA28_transitionS[i]);
		}
	}

	protected class DFA28 extends DFA {

		public DFA28(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 28;
			this.eot = DFA28_eot;
			this.eof = DFA28_eof;
			this.min = DFA28_min;
			this.max = DFA28_max;
			this.accept = DFA28_accept;
			this.special = DFA28_special;
			this.transition = DFA28_transition;
		}
		@Override
		public String getDescription() {
			return "1464:1: fragment NUMBER_MACR : ( ( '0' .. '9' )+ | ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ ) );";
		}
	}

	static final String DFA37_eotS =
		"\1\uffff\1\55\1\uffff\1\61\5\63\1\54\1\112\1\54\1\115\1\117\1\120\4\63"+
		"\1\133\1\135\6\uffff\1\146\10\uffff\1\160\1\uffff\1\163\1\uffff\1\63\2"+
		"\54\7\uffff\1\63\1\uffff\6\63\2\uffff\1\63\1\uffff\2\63\12\112\2\uffff"+
		"\1\120\4\uffff\1\75\1\120\2\uffff\1\75\1\uffff\1\63\1\uffff\1\63\36\uffff"+
		"\7\63\12\112\1\126\11\63\12\112\1\75\1\73\10\63\3\112\1\u00bd\6\112\1"+
		"\uffff\10\63\2\112\1\u00d0\1\uffff\10\112\10\63\1\112\1\u00e1\1\uffff"+
		"\12\112\5\63\1\112\1\uffff\14\112\1\63\1\u0100\1\uffff\1\u0102\5\112\1"+
		"\u0102\5\112\1\u010d\1\112\1\63\1\uffff\1\112\1\uffff\1\112\1\u0102\5"+
		"\112\1\u0102\2\112\1\uffff\1\u011a\1\63\1\112\1\u0102\10\112\2\uffff\2"+
		"\112\1\u0102\2\112\2\u0102\2\112\1\uffff\4\112\3\u0102\3\112\1\u0102\5"+
		"\112\1\u0102\3\112\1\u0102\4\112\1\u0102\1\112\1\u0102";
	static final String DFA37_eofS =
		"\u0142\uffff";
	static final String DFA37_minS =
		"\1\0\1\55\1\uffff\1\75\1\50\2\11\2\50\1\40\1\142\1\60\2\11\1\45\4\50\1"+
		"\41\1\55\6\uffff\1\52\10\uffff\1\75\1\uffff\1\75\1\uffff\1\50\2\75\7\uffff"+
		"\1\50\1\uffff\6\50\1\40\1\uffff\1\11\1\uffff\2\50\1\150\1\155\1\145\1"+
		"\141\2\157\1\145\2\151\1\157\2\uffff\1\45\4\uffff\1\55\1\45\1\60\1\uffff"+
		"\1\55\1\uffff\1\50\1\uffff\1\50\36\uffff\7\50\1\141\1\160\1\144\1\147"+
		"\1\160\1\164\1\146\1\147\1\145\1\156\1\11\11\50\1\162\1\157\1\151\1\145"+
		"\1\55\2\164\1\150\1\167\1\164\1\55\1\11\10\50\1\163\1\162\1\141\1\55\1"+
		"\143\1\157\1\55\1\164\1\160\1\55\1\uffff\10\50\1\145\1\164\1\55\1\uffff"+
		"\2\145\1\151\1\155\1\142\1\55\1\157\1\146\10\50\1\164\1\55\1\uffff\1\146"+
		"\1\156\1\147\1\55\1\157\1\151\1\157\1\142\1\162\1\141\5\50\1\11\1\uffff"+
		"\2\164\1\150\1\143\1\160\1\144\1\164\1\157\1\151\1\157\1\164\1\143\2\50"+
		"\1\uffff\1\55\1\145\1\164\2\145\1\151\1\55\1\144\1\164\1\160\1\144\1\164"+
		"\1\55\1\145\1\50\1\uffff\1\143\1\uffff\1\162\1\55\1\146\1\156\1\147\1"+
		"\154\1\157\1\55\1\144\1\164\1\uffff\1\55\1\50\1\157\1\55\1\143\2\164\1"+
		"\150\1\145\1\155\1\154\1\157\2\uffff\1\162\1\157\1\55\1\145\1\164\2\55"+
		"\1\145\1\155\1\uffff\1\156\1\162\1\143\1\162\3\55\1\145\1\156\1\157\1"+
		"\55\1\143\1\162\1\145\1\162\1\157\1\55\1\162\1\156\1\162\1\55\1\145\1"+
		"\156\1\162\1\145\1\55\1\162\1\55";
	static final String DFA37_maxS =
		"\1\uffff\1\ufffd\1\uffff\1\75\6\ufffd\1\166\10\ufffd\1\41\1\55\6\uffff"+
		"\1\57\10\uffff\1\75\1\uffff\1\75\1\uffff\1\ufffd\2\75\7\uffff\1\ufffd"+
		"\1\uffff\7\ufffd\1\uffff\1\ufffd\1\uffff\2\ufffd\1\150\1\155\1\145\1\141"+
		"\2\157\1\145\2\151\1\157\2\uffff\1\ufffd\4\uffff\2\ufffd\1\71\1\uffff"+
		"\1\ufffd\1\uffff\1\ufffd\1\uffff\1\ufffd\36\uffff\7\ufffd\1\141\1\160"+
		"\1\144\1\147\1\160\1\164\1\146\1\147\1\145\1\156\1\71\11\ufffd\1\162\1"+
		"\157\1\151\1\145\1\55\2\164\1\150\1\167\1\164\12\ufffd\1\163\1\162\1\141"+
		"\1\ufffd\1\162\1\157\1\55\1\164\1\160\1\55\1\uffff\10\ufffd\1\145\1\164"+
		"\1\ufffd\1\uffff\2\145\1\151\1\155\1\164\1\55\1\157\1\146\10\ufffd\1\164"+
		"\1\ufffd\1\uffff\1\146\1\156\1\147\1\55\1\157\1\151\1\157\1\164\1\162"+
		"\1\141\5\ufffd\1\47\1\uffff\2\164\1\150\1\162\1\160\1\144\1\164\1\157"+
		"\1\151\1\157\1\164\1\143\2\ufffd\1\uffff\1\ufffd\1\145\1\164\2\145\1\151"+
		"\1\ufffd\1\144\1\164\1\160\1\144\1\164\1\ufffd\1\145\1\ufffd\1\uffff\1"+
		"\143\1\uffff\1\162\1\ufffd\1\146\1\156\1\147\1\154\1\157\1\ufffd\1\144"+
		"\1\164\1\uffff\2\ufffd\1\157\1\ufffd\1\143\2\164\1\150\1\145\1\155\1\154"+
		"\1\157\2\uffff\1\162\1\157\1\ufffd\1\145\1\164\2\ufffd\1\145\1\155\1\uffff"+
		"\1\156\1\162\1\143\1\162\3\ufffd\1\145\1\156\1\157\1\ufffd\1\143\1\162"+
		"\1\145\1\162\1\157\1\ufffd\1\162\1\156\1\162\1\ufffd\1\145\1\156\1\162"+
		"\1\145\1\ufffd\1\162\1\ufffd";
	static final String DFA37_acceptS =
		"\2\uffff\1\2\22\uffff\1\31\1\32\1\33\1\34\1\35\1\36\1\uffff\1\40\1\42"+
		"\1\43\1\46\1\47\1\50\1\51\1\52\1\uffff\1\55\1\uffff\1\57\3\uffff\1\71"+
		"\1\72\1\1\1\20\1\2\1\66\1\3\1\uffff\1\5\7\uffff\1\63\1\uffff\1\21\14\uffff"+
		"\1\15\1\16\1\uffff\1\45\1\17\1\44\1\22\3\uffff\1\23\1\uffff\1\24\1\uffff"+
		"\1\26\1\uffff\1\27\1\41\1\30\1\54\1\31\1\32\1\33\1\34\1\35\1\36\1\60\1"+
		"\61\1\37\1\40\1\42\1\43\1\46\1\47\1\50\1\51\1\52\1\64\1\53\1\55\1\70\1"+
		"\56\1\57\1\65\1\67\1\71\71\uffff\1\25\13\uffff\1\11\22\uffff\1\10\20\uffff"+
		"\1\7\16\uffff\1\6\17\uffff\1\4\1\uffff\1\12\12\uffff\1\13\14\uffff\1\14"+
		"\1\62\11\uffff\1\62\34\uffff";
	static final String DFA37_specialS =
		"\1\0\u0141\uffff}>";
	static final String[] DFA37_transitionS = {
			"\11\53\5\47\22\53\1\47\1\43\1\14\1\1\1\52\1\31\1\2\1\15\1\37\1\40\1\46"+
			"\1\45\1\27\1\24\1\13\1\33\12\16\1\26\1\25\1\23\1\32\1\34\1\30\1\12\15"+
			"\22\1\6\6\22\1\20\5\22\1\41\1\11\1\42\1\3\1\7\1\54\4\50\1\21\3\50\1\4"+
			"\4\50\1\5\6\50\1\17\5\50\1\35\1\51\1\36\1\44\1\54\ud780\10\u0800\54\u1ffe"+
			"\10\2\54",
			"\1\56\2\uffff\12\56\7\uffff\32\56\1\uffff\1\56\2\uffff\1\56\1\uffff"+
			"\32\56\5\uffff\ud780\56\u0800\uffff\u1ffe\56",
			"",
			"\1\60",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\14\64\1\62\15\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\5\75\22\uffff\1\75\7\uffff\1\73\2\uffff\1\75\1\uffff\1\74\2\uffff\12"+
			"\66\7\uffff\32\65\1\uffff\1\72\2\uffff\1\70\1\uffff\32\64\5\uffff\ud780"+
			"\71\u0800\uffff\u1ffe\71",
			"\5\75\22\uffff\1\75\7\uffff\1\73\2\uffff\1\75\1\uffff\1\74\2\uffff\12"+
			"\66\7\uffff\32\65\1\uffff\1\72\2\uffff\1\70\1\uffff\32\64\5\uffff\ud780"+
			"\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\20\77\12\76\7\77\6\76\32\77\6\76\30\77\1\uffff\ud780\77\u0800\uffff"+
			"\u1ffe\77",
			"\1\105\1\100\2\uffff\1\111\2\uffff\1\101\2\uffff\1\106\1\102\2\uffff"+
			"\1\103\1\uffff\1\107\1\uffff\1\104\1\uffff\1\110",
			"\12\114\7\uffff\32\113\1\uffff\1\113\2\uffff\1\113\1\uffff\32\113\5"+
			"\uffff\ud780\113\u0800\uffff\u1ffe\113",
			"\1\116\26\uffff\137\116\1\uffff\ud780\116\u0800\uffff\u1ffe\116",
			"\1\116\26\uffff\137\116\1\uffff\ud780\116\u0800\uffff\u1ffe\116",
			"\1\124\10\uffff\1\123\1\uffff\12\122\7\uffff\15\126\1\125\14\126\1\uffff"+
			"\1\126\2\uffff\1\126\1\uffff\15\126\1\121\14\126\5\uffff\ud780\126\u0800"+
			"\uffff\u1ffe\126",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\21\64\1\127\10\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\2\uffff\1\130\1\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff"+
			"\1\72\2\uffff\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\27\64\1\131\2\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\132",
			"\1\134",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\144\4\uffff\1\145",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\157",
			"",
			"\1\162",
			"",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\165",
			"\1\166",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\17\64\1\170\12\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\20\172\12\171\7\172\6\171\32\172\6\171\30\172\1\uffff\ud780\172\u0800"+
			"\uffff\u1ffe\172",
			"",
			"\5\75\22\uffff\1\75\7\uffff\1\73\4\uffff\1\67\2\uffff\12\173\7\uffff"+
			"\32\65\1\uffff\1\72\2\uffff\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800"+
			"\uffff\u1ffe\71",
			"",
			"\1\73\4\uffff\1\67\2\uffff\12\176\7\uffff\6\175\24\65\1\uffff\1\72\2"+
			"\uffff\1\70\1\uffff\6\174\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\177",
			"\1\u0080",
			"\1\u0081",
			"\1\u0082",
			"\1\u0083",
			"\1\u0084",
			"\1\u0085",
			"\1\u0086",
			"\1\u0087",
			"\1\u0088",
			"",
			"",
			"\1\124\12\uffff\12\114\7\uffff\32\126\1\uffff\1\126\2\uffff\1\126\1"+
			"\uffff\32\126\5\uffff\ud780\126\u0800\uffff\u1ffe\126",
			"",
			"",
			"",
			"",
			"\1\u0089\2\uffff\12\126\7\uffff\32\126\1\uffff\1\126\2\uffff\1\126\1"+
			"\uffff\32\126\5\uffff\ud780\126\u0800\uffff\u1ffe\126",
			"\1\124\10\uffff\1\123\1\uffff\12\122\7\uffff\15\126\1\125\14\126\1\uffff"+
			"\1\126\2\uffff\1\126\1\uffff\15\126\1\121\14\126\5\uffff\ud780\126\u0800"+
			"\uffff\u1ffe\126",
			"\12\114",
			"",
			"\1\u0089\2\uffff\12\126\7\uffff\32\126\1\uffff\1\126\2\uffff\1\126\1"+
			"\uffff\32\126\5\uffff\ud780\126\u0800\uffff\u1ffe\126",
			"",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\13\64\1\u008a\16\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\17\64\1\u008b\12\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\16\64\1\u008c\13\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u008f\7\uffff\6\u008e\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u008d\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\173\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u0092\7\uffff\6\u0091\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u0090\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u0092\7\uffff\6\u0091\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u0090\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u0092\7\uffff\6\u0091\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u0090\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\u0093",
			"\1\u0094",
			"\1\u0095",
			"\1\u0096",
			"\1\u0097",
			"\1\u0098",
			"\1\u0099",
			"\1\u009a",
			"\1\u009b",
			"\1\u009c",
			"\5\75\22\uffff\1\75\17\uffff\12\u009d",
			"\1\u009e\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\21\64\1\u009f\10\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\21\64\1\u00a0\10\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00a3\7\uffff\6\u00a2\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00a1\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00a3\7\uffff\6\u00a2\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00a1\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00a3\7\uffff\6\u00a2\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00a1\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00a6\7\uffff\6\u00a5\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00a4\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00a6\7\uffff\6\u00a5\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00a4\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00a6\7\uffff\6\u00a5\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00a4\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\u00a7",
			"\1\u00a8",
			"\1\u00a9",
			"\1\u00aa",
			"\1\u00ab",
			"\1\u00ac",
			"\1\u00ad",
			"\1\u00ae",
			"\1\u00af",
			"\1\u00b0",
			"\1\126\2\uffff\12\u009d\7\uffff\32\126\1\uffff\1\126\2\uffff\1\126\1"+
			"\uffff\32\126\5\uffff\ud780\126\u0800\uffff\u1ffe\126",
			"\5\u00b1\22\uffff\10\u00b1\1\uffff\126\u00b1\1\uffff\ud780\u00b1\u0800"+
			"\uffff\u1ffe\u00b1",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\4\64\1\u00b2\25\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\23\64\1\u00b3\6\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00b6\7\uffff\6\u00b5\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00b4\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00b6\7\uffff\6\u00b5\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00b4\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00b6\7\uffff\6\u00b5\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00b4\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00b9\7\uffff\6\u00b8\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00b7\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00b9\7\uffff\6\u00b8\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00b7\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00b9\7\uffff\6\u00b8\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00b7\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\u00ba",
			"\1\u00bb",
			"\1\u00bc",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u00bf\10\uffff\1\u00be\5\uffff\1\u00c0",
			"\1\u00c1",
			"\1\u00c2",
			"\1\u00c3",
			"\1\u00c4",
			"\1\u00c5",
			"",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\22\64\1\u00c6\7\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\1\u00c7\31\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00ca\7\uffff\6\u00c9\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00c8\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00ca\7\uffff\6\u00c9\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00c8\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00ca\7\uffff\6\u00c9\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00c8\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00cd\7\uffff\6\u00cc\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00cb\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00cd\7\uffff\6\u00cc\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00cb\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00cd\7\uffff\6\u00cc\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00cb\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\u00ce",
			"\1\u00cf",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"",
			"\1\u00d1",
			"\1\u00d2",
			"\1\u00d3",
			"\1\u00d4",
			"\1\u00d7\12\uffff\1\u00d6\6\uffff\1\u00d5",
			"\1\u00d8",
			"\1\u00d9",
			"\1\u00da",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\22\64\1\u00db\7\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\15\64\1\u00dc\14\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00df\7\uffff\6\u00de\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00dd\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00df\7\uffff\6\u00de\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00dd\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\u00df\7\uffff\6\u00de\24\65\1\uffff\1"+
			"\72\2\uffff\1\70\1\uffff\6\u00dd\24\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\u00e0",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"",
			"\1\u00e2",
			"\1\u00e3",
			"\1\u00e4",
			"\1\u00e5",
			"\1\u00e6",
			"\1\u00e7",
			"\1\u00e8",
			"\1\u00eb\12\uffff\1\u00ea\6\uffff\1\u00e9",
			"\1\u00ec",
			"\1\u00ed",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\10\64\1\u00ee\21\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\23\64\1\u00ef\6\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\5\u00f0\22\uffff\1\u00f0\1\uffff\1\u00f0\4\uffff\1\u00f0",
			"",
			"\1\u00f1",
			"\1\u00f2",
			"\1\u00f3",
			"\1\u00f5\10\uffff\1\u00f4\5\uffff\1\u00f6",
			"\1\u00f7",
			"\1\u00f8",
			"\1\u00f9",
			"\1\u00fa",
			"\1\u00fb",
			"\1\u00fc",
			"\1\u00fd",
			"\1\u00fe",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\16\64\1\u00ff\13\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"",
			"\1\u0101\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1"+
			"\uffff\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u0103",
			"\1\u0104",
			"\1\u0105",
			"\1\u0106",
			"\1\u0107",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u0108",
			"\1\u0109",
			"\1\u010a",
			"\1\u010b",
			"\1\u010c",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u010e",
			"\1\73\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\15\64\1\u010f\14\64\5\uffff\ud780\71\u0800\uffff\u1ffe"+
			"\71",
			"",
			"\1\u0110",
			"",
			"\1\u0111",
			"\1\u0112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1"+
			"\uffff\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u0113",
			"\1\u0114",
			"\1\u0115",
			"\1\u0116",
			"\1\u0117",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u0118",
			"\1\u0119",
			"",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u011b\4\uffff\1\67\2\uffff\12\66\7\uffff\32\65\1\uffff\1\72\2\uffff"+
			"\1\70\1\uffff\32\64\5\uffff\ud780\71\u0800\uffff\u1ffe\71",
			"\1\u011c",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u011d",
			"\1\u011e",
			"\1\u011f",
			"\1\u0120",
			"\1\u0121",
			"\1\u0122",
			"\1\u0123",
			"\1\u0124",
			"",
			"",
			"\1\u0126",
			"\1\u0127",
			"\1\u0128\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1"+
			"\uffff\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u0129",
			"\1\u012a",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u012b",
			"\1\u012c",
			"",
			"\1\u012d",
			"\1\u012e",
			"\1\u012f",
			"\1\u0130",
			"\1\u0131\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1"+
			"\uffff\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u0132",
			"\1\u0133",
			"\1\u0134",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u0135",
			"\1\u0136",
			"\1\u0137",
			"\1\u0138",
			"\1\u0139",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u013a",
			"\1\u013b",
			"\1\u013c",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u013d",
			"\1\u013e",
			"\1\u013f",
			"\1\u0140",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112",
			"\1\u0141",
			"\1\112\2\uffff\12\112\7\uffff\32\112\1\uffff\1\112\2\uffff\1\112\1\uffff"+
			"\32\112\5\uffff\ud780\112\u0800\uffff\u1ffe\112"
	};

	static final short[] DFA37_eot = DFA.unpackEncodedString(DFA37_eotS);
	static final short[] DFA37_eof = DFA.unpackEncodedString(DFA37_eofS);
	static final char[] DFA37_min = DFA.unpackEncodedStringToUnsignedChars(DFA37_minS);
	static final char[] DFA37_max = DFA.unpackEncodedStringToUnsignedChars(DFA37_maxS);
	static final short[] DFA37_accept = DFA.unpackEncodedString(DFA37_acceptS);
	static final short[] DFA37_special = DFA.unpackEncodedString(DFA37_specialS);
	static final short[][] DFA37_transition;

	static {
		int numStates = DFA37_transitionS.length;
		DFA37_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA37_transition[i] = DFA.unpackEncodedString(DFA37_transitionS[i]);
		}
	}

	protected class DFA37 extends DFA {

		public DFA37(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 37;
			this.eot = DFA37_eot;
			this.eof = DFA37_eof;
			this.min = DFA37_min;
			this.max = DFA37_max;
			this.accept = DFA37_accept;
			this.special = DFA37_special;
			this.transition = DFA37_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__101 | T__102 | T__103 | T__104 | IDENT | CHARSET | IMPORT | MEDIA | PAGE | MARGIN_AREA | VIEWPORT | FONTFACE | ATKEYWORD | CLASSKEYWORD | STRING | HASH | INDEX | NUMBER | PERCENTAGE | DIMENSION | URI | UNIRANGE | CDO | CDC | SEMICOLON | COLON | COMMA | QUESTION | PERCENT | EQUALS | SLASH | GREATER | LESS | LCURLY | RCURLY | APOS | QUOT | LPAREN | RPAREN | LBRACE | RBRACE | EXCLAMATION | TILDE | MINUS | PLUS | ASTERISK | S | COMMENT | SL_COMMENT | EXPRESSION | FUNCTION | INCLUDES | DASHMATCH | STARTSWITH | ENDSWITH | CONTAINS | CTRL | INVALID_TOKEN );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA37_0 = input.LA(1);
						s = -1;
						if ( (LA37_0=='#') ) {s = 1;}
						else if ( (LA37_0=='&') ) {s = 2;}
						else if ( (LA37_0=='^') ) {s = 3;}
						else if ( (LA37_0=='i') ) {s = 4;}
						else if ( (LA37_0=='n') ) {s = 5;}
						else if ( (LA37_0=='N') ) {s = 6;}
						else if ( (LA37_0=='_') ) {s = 7;}
						else if ( ((LA37_0 >= '\u0080' && LA37_0 <= '\uD7FF')||(LA37_0 >= '\uE000' && LA37_0 <= '\uFFFD')) ) {s = 8;}
						else if ( (LA37_0=='\\') ) {s = 9;}
						else if ( (LA37_0=='@') ) {s = 10;}
						else if ( (LA37_0=='.') ) {s = 11;}
						else if ( (LA37_0=='\"') ) {s = 12;}
						else if ( (LA37_0=='\'') ) {s = 13;}
						else if ( ((LA37_0 >= '0' && LA37_0 <= '9')) ) {s = 14;}
						else if ( (LA37_0=='u') ) {s = 15;}
						else if ( (LA37_0=='U') ) {s = 16;}
						else if ( (LA37_0=='e') ) {s = 17;}
						else if ( ((LA37_0 >= 'A' && LA37_0 <= 'M')||(LA37_0 >= 'O' && LA37_0 <= 'T')||(LA37_0 >= 'V' && LA37_0 <= 'Z')) ) {s = 18;}
						else if ( (LA37_0=='<') ) {s = 19;}
						else if ( (LA37_0=='-') ) {s = 20;}
						else if ( (LA37_0==';') ) {s = 21;}
						else if ( (LA37_0==':') ) {s = 22;}
						else if ( (LA37_0==',') ) {s = 23;}
						else if ( (LA37_0=='?') ) {s = 24;}
						else if ( (LA37_0=='%') ) {s = 25;}
						else if ( (LA37_0=='=') ) {s = 26;}
						else if ( (LA37_0=='/') ) {s = 27;}
						else if ( (LA37_0=='>') ) {s = 28;}
						else if ( (LA37_0=='{') ) {s = 29;}
						else if ( (LA37_0=='}') ) {s = 30;}
						else if ( (LA37_0=='(') ) {s = 31;}
						else if ( (LA37_0==')') ) {s = 32;}
						else if ( (LA37_0=='[') ) {s = 33;}
						else if ( (LA37_0==']') ) {s = 34;}
						else if ( (LA37_0=='!') ) {s = 35;}
						else if ( (LA37_0=='~') ) {s = 36;}
						else if ( (LA37_0=='+') ) {s = 37;}
						else if ( (LA37_0=='*') ) {s = 38;}
						else if ( ((LA37_0 >= '\t' && LA37_0 <= '\r')||LA37_0==' ') ) {s = 39;}
						else if ( ((LA37_0 >= 'a' && LA37_0 <= 'd')||(LA37_0 >= 'f' && LA37_0 <= 'h')||(LA37_0 >= 'j' && LA37_0 <= 'm')||(LA37_0 >= 'o' && LA37_0 <= 't')||(LA37_0 >= 'v' && LA37_0 <= 'z')) ) {s = 40;}
						else if ( (LA37_0=='|') ) {s = 41;}
						else if ( (LA37_0=='$') ) {s = 42;}
						else if ( ((LA37_0 >= '\u0000' && LA37_0 <= '\b')||(LA37_0 >= '\u000E' && LA37_0 <= '\u001F')) ) {s = 43;}
						else if ( (LA37_0=='`'||LA37_0=='\u007F'||(LA37_0 >= '\uD800' && LA37_0 <= '\uDFFF')||(LA37_0 >= '\uFFFE' && LA37_0 <= '\uFFFF')) ) {s = 44;}
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 37, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
