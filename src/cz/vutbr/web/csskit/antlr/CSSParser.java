// $ANTLR 3.5.2 cz/vutbr/web/csskit/antlr/CSS.g 2014-07-11 12:43:53
 
package cz.vutbr.web.csskit.antlr;

import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.SupportedCSS;
import cz.vutbr.web.csskit.antlr.CSSLexer.LexerState;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A basic CSS grammar.
 */
@SuppressWarnings("all")
public class CSSParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ADJACENT", "APOS", "ASTERISK", 
		"ATBLOCK", "ATKEYWORD", "ATTRIBUTE", "BRACEBLOCK", "CDC", "CDO", "CHARSET", 
		"CHILD", "CLASSKEYWORD", "COLON", "COMMA", "COMMENT", "CONTAINS", "CTRL", 
		"CTRL_CHAR", "CURLYBLOCK", "DASHMATCH", "DECLARATION", "DESCENDANT", "DIMENSION", 
		"ELEMENT", "ENDSWITH", "EQUALS", "ESCAPE_CHAR", "EXCLAMATION", "EXPRESSION", 
		"FONTFACE", "FUNCTION", "GREATER", "HASH", "IDENT", "IDENT_MACR", "IMPORT", 
		"IMPORTANT", "INCLUDES", "INDEX", "INLINESTYLE", "INTEGER_MACR", "INVALID_DECLARATION", 
		"INVALID_DIRECTIVE", "INVALID_IMPORT", "INVALID_SELECTOR", "INVALID_SELPART", 
		"INVALID_STATEMENT", "INVALID_STRING", "INVALID_TOKEN", "LBRACE", "LCURLY", 
		"LESS", "LPAREN", "MARGIN_AREA", "MEDIA", "MEDIA_QUERY", "MINUS", "NAME_CHAR", 
		"NAME_MACR", "NAME_START", "NL_CHAR", "NON_ASCII", "NUMBER", "NUMBER_MACR", 
		"PAGE", "PARENBLOCK", "PERCENT", "PERCENTAGE", "PLUS", "PRECEDING", "PSEUDO", 
		"QUESTION", "QUOT", "RBRACE", "RCURLY", "RPAREN", "RULE", "S", "SELECTOR", 
		"SEMICOLON", "SET", "SLASH", "SL_COMMENT", "STARTSWITH", "STRING", "STRING_CHAR", 
		"STRING_MACR", "STYLESHEET", "TILDE", "UNIRANGE", "URI", "URI_CHAR", "URI_MACR", 
		"VALUE", "VIEWPORT", "W_CHAR", "W_MACR", "'#'", "'&'", "'^'", "'important'"
	};
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

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public CSSParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public CSSParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return CSSParser.tokenNames; }
	@Override public String getGrammarFileName() { return "cz/vutbr/web/csskit/antlr/CSS.g"; }


	    private static Logger log = LoggerFactory.getLogger(CSSParser.class);
	    
	    private static SupportedCSS css = CSSFactory.getSupportedCSS();
	    
	    private int functLevel = 0;
	    
	    /**
	     * This function must be called to initialize parser's state.
	     * Because we can't change directly generated constructors.
	     */
	    public CSSParser init() {
	    	return this;
	    }
	    
	    @Override
	    public void emitErrorMessage(String msg) {
	    	log.info("ANTLR: {}", msg);
		}    

		private Object invalidReplacement(int ttype, String ttext) {
			
			Object root = (Object) adaptor.nil();
			Object node = (Object) adaptor.create(ttype, ttext);
			
			adaptor.addChild(root, node);	
			
			if(log.isDebugEnabled()) {
				log.debug("Invalid fallback with: {}", TreeUtil.toStringTree((CommonTree) root));
			}
			
			return root;	
		}

		/**
		 * Recovers and logs error, prepares tree part replacement
		 */ 
		private Object invalidFallback(int ttype, String ttext, RecognitionException re) {
		    reportError(re);
			recover(input, re);
			return invalidReplacement(ttype, ttext);
		}
		
		/**
		 * Recovers and logs error, using custom follow set,
		 * prepares tree part replacement
		 */ 
		private Object invalidFallbackGreedy(int ttype, String ttext, BitSet follow, RecognitionException re) {
			reportError(re);
			if ( state.lastErrorIndex==input.index() ) {
				// uh oh, another error at same token index; must be a case
		 		// where LT(1) is in the recovery token set so nothing is
	            // consumed; consume a single token so at least to prevent
	            // an infinite loop; this is a failsafe.
	            input.consume();
	        }
	    state.lastErrorIndex = input.index();
	    beginResync();
			consumeUntilGreedy(input, follow);
	    endResync();
			return invalidReplacement(ttype, ttext);
			
	    }
		
		/**
		 * Consumes token until lexer state is balanced and
		 * token from follow is matched. Matched token is also consumed
		 */ 
		private void consumeUntilGreedy(TokenStream input, BitSet follow) {
			CSSToken t = null;
			do{
			  Token next = input.LT(1);
			  if (next instanceof CSSToken)
			      t= (CSSToken) input.LT(1);
			  else
			      break; /* not a CSSToken, probably EOF */
			  log.trace("Skipped greedy: {} follow: {}", t, follow);
			  // consume token even if it will match
			  input.consume();
			}while(!(t.getLexerState().isBalanced() && follow.member(t.getType())));
		} 

	  /**
	   * Recovers and logs error inside a function, using custom follow set,
	   * prepares tree part replacement
	   */ 
	  private Object invalidFallbackGreedy(int ttype, String ttext, BitSet follow, LexerState.RecoveryMode mode, LexerState ls, RecognitionException re) {
	    reportError(re);
	    if ( state.lastErrorIndex==input.index() ) {
	      // uh oh, another error at same token index; must be a case
	      // where LT(1) is in the recovery token set so nothing is
	            // consumed; consume a single token so at least to prevent
	            // an infinite loop; this is a failsafe.
	            input.consume();
	        }
	    state.lastErrorIndex = input.index();
	    beginResync();
	    consumeUntilGreedy(input, follow, mode, ls);
	    endResync();
	    return invalidReplacement(ttype, ttext);
	    
	    }
	  
	  /**
	   * Consumes token until lexer state is function-balanced and
	   * token from follow is matched. Matched token is also consumed
	   */ 
	  private void consumeUntilGreedy(TokenStream input, BitSet follow, LexerState.RecoveryMode mode, LexerState ls) {
	    CSSToken t = null;
	    do{
	      Token next = input.LT(1);
	      if (next instanceof CSSToken)
	          t= (CSSToken) input.LT(1);
	      else
	          break; /* not a CSSToken, probably EOF */
	      log.trace("Skipped greedy: {}", t);
	      // consume token even if it will match
	      input.consume();
	    }while(!(t.getLexerState().isBalanced(mode, ls, t) && follow.member(t.getType())));
	  }
	  
	  /**
	   * Recovers and logs error inside a function, using custom follow set,
	   * prepares tree part replacement
	   */ 
	  private Object invalidFallback(int ttype, String ttext, BitSet follow, LexerState.RecoveryMode mode, LexerState ls, RecognitionException re) {
	    reportError(re);
	    if ( state.lastErrorIndex==input.index() ) {
	      // uh oh, another error at same token index; must be a case
	      // where LT(1) is in the recovery token set so nothing is
	            // consumed; consume a single token so at least to prevent
	            // an infinite loop; this is a failsafe.
	            input.consume();
	        }
	    state.lastErrorIndex = input.index();
	    beginResync();
	    consumeUntil(input, follow, mode, ls);
	    endResync();
	    return invalidReplacement(ttype, ttext);
	    
	    }
	  
	  /**
	   * Consumes token until lexer state is function-balanced and
	   * token from follow is matched.
	   */ 
	  private void consumeUntil(TokenStream input, BitSet follow, LexerState.RecoveryMode mode, LexerState ls) {
	    CSSToken t = null;
	    boolean finish = false;
	    do{
	      Token next = input.LT(1);
	      if (next instanceof CSSToken)
	          t= (CSSToken) input.LT(1);
	      else
	          break; /* not a CSSToken, probably EOF */
	      // consume token if does not match
	      finish = (t.getLexerState().isBalanced(mode, ls, t) && follow.member(t.getType()));
	      if (!finish)
	      { 
	          log.trace("Skipped: {}", t);
	          input.consume();
	      }
	    }while(!finish);
	  }
	    
	  /**
	   * Obtains the current lexer state from current token
	   */
	  private LexerState getCurrentLexerState(Token t)
	  {
	      if (t instanceof CSSToken)
	          return ((CSSToken) t).getLexerState();
	      else
	          return null;
	  }
	     
	  //this switches the single token insertion / deletion off because it interferes with our own error recovery
	  protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
	      throws RecognitionException
	  {
	      throw new MismatchedTokenException(ttype, input);
	  }
	   


	public static class inlinestyle_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "inlinestyle"
	// cz/vutbr/web/csskit/antlr/CSS.g:731:1: inlinestyle : ( S )* ( declarations -> ^( INLINESTYLE declarations ) | ( inlineset )+ -> ^( INLINESTYLE ( inlineset )+ ) ) ;
	public final CSSParser.inlinestyle_return inlinestyle() throws RecognitionException {
		CSSParser.inlinestyle_return retval = new CSSParser.inlinestyle_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token S1=null;
		ParserRuleReturnScope declarations2 =null;
		ParserRuleReturnScope inlineset3 =null;

		Object S1_tree=null;
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleSubtreeStream stream_inlineset=new RewriteRuleSubtreeStream(adaptor,"rule inlineset");
		RewriteRuleSubtreeStream stream_declarations=new RewriteRuleSubtreeStream(adaptor,"rule declarations");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:732:2: ( ( S )* ( declarations -> ^( INLINESTYLE declarations ) | ( inlineset )+ -> ^( INLINESTYLE ( inlineset )+ ) ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:732:4: ( S )* ( declarations -> ^( INLINESTYLE declarations ) | ( inlineset )+ -> ^( INLINESTYLE ( inlineset )+ ) )
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:732:4: ( S )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==S) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:732:4: S
					{
					S1=(Token)match(input,S,FOLLOW_S_in_inlinestyle203);  
					stream_S.add(S1);

					}
					break;

				default :
					break loop1;
				}
			}

			// cz/vutbr/web/csskit/antlr/CSS.g:732:8: ( declarations -> ^( INLINESTYLE declarations ) | ( inlineset )+ -> ^( INLINESTYLE ( inlineset )+ ) )
			int alt3=2;
			switch ( input.LA(1) ) {
			case EOF:
			case ASTERISK:
			case CLASSKEYWORD:
			case COMMA:
			case CTRL:
			case DASHMATCH:
			case EQUALS:
			case EXCLAMATION:
			case GREATER:
			case IDENT:
			case INCLUDES:
			case INVALID_TOKEN:
			case LESS:
			case MINUS:
			case NUMBER:
			case PERCENT:
			case PLUS:
			case QUESTION:
			case SEMICOLON:
			case SLASH:
			case STRING_CHAR:
				{
				alt3=1;
				}
				break;
			case COLON:
				{
				switch ( input.LA(2) ) {
				case EOF:
				case ASTERISK:
				case CLASSKEYWORD:
				case COMMA:
				case DASHMATCH:
				case DIMENSION:
				case EQUALS:
				case EXCLAMATION:
				case GREATER:
				case HASH:
				case INCLUDES:
				case INVALID_STRING:
				case LBRACE:
				case LESS:
				case LPAREN:
				case MINUS:
				case NUMBER:
				case PERCENT:
				case PERCENTAGE:
				case PLUS:
				case QUESTION:
				case S:
				case SEMICOLON:
				case SLASH:
				case STRING:
				case UNIRANGE:
				case URI:
					{
					alt3=1;
					}
					break;
				case IDENT:
					{
					alt3=1;
					}
					break;
				case COLON:
					{
					alt3=1;
					}
					break;
				case FUNCTION:
					{
					alt3=1;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case LCURLY:
				{
				alt3=2;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}
			switch (alt3) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:732:9: declarations
					{
					pushFollow(FOLLOW_declarations_in_inlinestyle208);
					declarations2=declarations();
					state._fsp--;

					stream_declarations.add(declarations2.getTree());
					// AST REWRITE
					// elements: declarations
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 732:22: -> ^( INLINESTYLE declarations )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:732:25: ^( INLINESTYLE declarations )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(INLINESTYLE, "INLINESTYLE"), root_1);
						adaptor.addChild(root_1, stream_declarations.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:733:10: ( inlineset )+
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:733:10: ( inlineset )+
					int cnt2=0;
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( (LA2_0==COLON||LA2_0==LCURLY) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:733:10: inlineset
							{
							pushFollow(FOLLOW_inlineset_in_inlinestyle228);
							inlineset3=inlineset();
							state._fsp--;

							stream_inlineset.add(inlineset3.getTree());
							}
							break;

						default :
							if ( cnt2 >= 1 ) break loop2;
							EarlyExitException eee = new EarlyExitException(2, input);
							throw eee;
						}
						cnt2++;
					}

					// AST REWRITE
					// elements: inlineset
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 733:21: -> ^( INLINESTYLE ( inlineset )+ )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:733:24: ^( INLINESTYLE ( inlineset )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(INLINESTYLE, "INLINESTYLE"), root_1);
						if ( !(stream_inlineset.hasNext()) ) {
							throw new RewriteEarlyExitException();
						}
						while ( stream_inlineset.hasNext() ) {
							adaptor.addChild(root_1, stream_inlineset.nextTree());
						}
						stream_inlineset.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "inlinestyle"


	public static class stylesheet_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "stylesheet"
	// cz/vutbr/web/csskit/antlr/CSS.g:737:1: stylesheet : ( CDO | CDC | S | nostatement | statement )* -> ^( STYLESHEET ( statement )* ) ;
	public final CSSParser.stylesheet_return stylesheet() throws RecognitionException {
		CSSParser.stylesheet_return retval = new CSSParser.stylesheet_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CDO4=null;
		Token CDC5=null;
		Token S6=null;
		ParserRuleReturnScope nostatement7 =null;
		ParserRuleReturnScope statement8 =null;

		Object CDO4_tree=null;
		Object CDC5_tree=null;
		Object S6_tree=null;
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_CDO=new RewriteRuleTokenStream(adaptor,"token CDO");
		RewriteRuleTokenStream stream_CDC=new RewriteRuleTokenStream(adaptor,"token CDC");
		RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
		RewriteRuleSubtreeStream stream_nostatement=new RewriteRuleSubtreeStream(adaptor,"rule nostatement");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:738:2: ( ( CDO | CDC | S | nostatement | statement )* -> ^( STYLESHEET ( statement )* ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:738:4: ( CDO | CDC | S | nostatement | statement )*
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:738:4: ( CDO | CDC | S | nostatement | statement )*
			loop4:
			while (true) {
				int alt4=6;
				switch ( input.LA(1) ) {
				case CDO:
					{
					alt4=1;
					}
					break;
				case CDC:
					{
					alt4=2;
					}
					break;
				case S:
					{
					alt4=3;
					}
					break;
				case APOS:
				case QUOT:
				case RCURLY:
				case SEMICOLON:
					{
					alt4=4;
					}
					break;
				case ASTERISK:
				case ATKEYWORD:
				case CHARSET:
				case CLASSKEYWORD:
				case COLON:
				case COMMA:
				case CTRL:
				case DASHMATCH:
				case DIMENSION:
				case EQUALS:
				case EXCLAMATION:
				case FONTFACE:
				case GREATER:
				case HASH:
				case IDENT:
				case IMPORT:
				case INCLUDES:
				case INVALID_SELPART:
				case INVALID_STRING:
				case LBRACE:
				case LESS:
				case MEDIA:
				case MINUS:
				case NUMBER:
				case PAGE:
				case PERCENT:
				case PERCENTAGE:
				case PLUS:
				case QUESTION:
				case RPAREN:
				case SLASH:
				case STRING:
				case UNIRANGE:
				case URI:
				case VIEWPORT:
				case 101:
				case 102:
				case 103:
					{
					alt4=5;
					}
					break;
				}
				switch (alt4) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:738:6: CDO
					{
					CDO4=(Token)match(input,CDO,FOLLOW_CDO_in_stylesheet256);  
					stream_CDO.add(CDO4);

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:738:12: CDC
					{
					CDC5=(Token)match(input,CDC,FOLLOW_CDC_in_stylesheet260);  
					stream_CDC.add(CDC5);

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:738:18: S
					{
					S6=(Token)match(input,S,FOLLOW_S_in_stylesheet264);  
					stream_S.add(S6);

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:738:22: nostatement
					{
					pushFollow(FOLLOW_nostatement_in_stylesheet268);
					nostatement7=nostatement();
					state._fsp--;

					stream_nostatement.add(nostatement7.getTree());
					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:738:36: statement
					{
					pushFollow(FOLLOW_statement_in_stylesheet272);
					statement8=statement();
					state._fsp--;

					stream_statement.add(statement8.getTree());
					}
					break;

				default :
					break loop4;
				}
			}

			// AST REWRITE
			// elements: statement
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 739:3: -> ^( STYLESHEET ( statement )* )
			{
				// cz/vutbr/web/csskit/antlr/CSS.g:739:6: ^( STYLESHEET ( statement )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STYLESHEET, "STYLESHEET"), root_1);
				// cz/vutbr/web/csskit/antlr/CSS.g:739:19: ( statement )*
				while ( stream_statement.hasNext() ) {
					adaptor.addChild(root_1, stream_statement.nextTree());
				}
				stream_statement.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "stylesheet"


	public static class statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "statement"
	// cz/vutbr/web/csskit/antlr/CSS.g:742:1: statement : ( ruleset | atstatement );
	public final CSSParser.statement_return statement() throws RecognitionException {
		CSSParser.statement_return retval = new CSSParser.statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope ruleset9 =null;
		ParserRuleReturnScope atstatement10 =null;


		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:743:2: ( ruleset | atstatement )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==ASTERISK||(LA5_0 >= CLASSKEYWORD && LA5_0 <= COMMA)||LA5_0==CTRL||LA5_0==DASHMATCH||LA5_0==DIMENSION||LA5_0==EQUALS||LA5_0==EXCLAMATION||(LA5_0 >= GREATER && LA5_0 <= IDENT)||LA5_0==INCLUDES||LA5_0==INVALID_SELPART||LA5_0==INVALID_STRING||LA5_0==LBRACE||LA5_0==LESS||LA5_0==MINUS||LA5_0==NUMBER||(LA5_0 >= PERCENT && LA5_0 <= PLUS)||LA5_0==QUESTION||LA5_0==RPAREN||LA5_0==SLASH||LA5_0==STRING||(LA5_0 >= UNIRANGE && LA5_0 <= URI)||(LA5_0 >= 101 && LA5_0 <= 103)) ) {
				alt5=1;
			}
			else if ( (LA5_0==ATKEYWORD||LA5_0==CHARSET||LA5_0==FONTFACE||LA5_0==IMPORT||LA5_0==MEDIA||LA5_0==PAGE||LA5_0==VIEWPORT) ) {
				alt5=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}

			switch (alt5) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:743:4: ruleset
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_ruleset_in_statement302);
					ruleset9=ruleset();
					state._fsp--;

					adaptor.addChild(root_0, ruleset9.getTree());

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:743:14: atstatement
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_atstatement_in_statement306);
					atstatement10=atstatement();
					state._fsp--;

					adaptor.addChild(root_0, atstatement10.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "statement"


	public static class atstatement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "atstatement"
	// cz/vutbr/web/csskit/antlr/CSS.g:746:1: atstatement : ( CHARSET | IMPORT ( S )* import_uri ( S )* ( media )? SEMICOLON -> ^( IMPORT ( media )? import_uri ) | page | VIEWPORT ( S )* LCURLY ( S )* declarations RCURLY -> ^( VIEWPORT declarations ) | FONTFACE ( S )* LCURLY ( S )* declarations RCURLY -> ^( FONTFACE declarations ) | MEDIA ( S )* ( media )? LCURLY ( S )* ( media_rule ( S )* )* RCURLY -> ^( MEDIA ( media )? ( media_rule )* ) | ATKEYWORD ( S )* LCURLY ( any )* RCURLY -> INVALID_STATEMENT );
	public final CSSParser.atstatement_return atstatement() throws RecognitionException {
		CSSParser.atstatement_return retval = new CSSParser.atstatement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CHARSET11=null;
		Token IMPORT12=null;
		Token S13=null;
		Token S15=null;
		Token SEMICOLON17=null;
		Token VIEWPORT19=null;
		Token S20=null;
		Token LCURLY21=null;
		Token S22=null;
		Token RCURLY24=null;
		Token FONTFACE25=null;
		Token S26=null;
		Token LCURLY27=null;
		Token S28=null;
		Token RCURLY30=null;
		Token MEDIA31=null;
		Token S32=null;
		Token LCURLY34=null;
		Token S35=null;
		Token S37=null;
		Token RCURLY38=null;
		Token ATKEYWORD39=null;
		Token S40=null;
		Token LCURLY41=null;
		Token RCURLY43=null;
		ParserRuleReturnScope import_uri14 =null;
		ParserRuleReturnScope media16 =null;
		ParserRuleReturnScope page18 =null;
		ParserRuleReturnScope declarations23 =null;
		ParserRuleReturnScope declarations29 =null;
		ParserRuleReturnScope media33 =null;
		ParserRuleReturnScope media_rule36 =null;
		ParserRuleReturnScope any42 =null;

		Object CHARSET11_tree=null;
		Object IMPORT12_tree=null;
		Object S13_tree=null;
		Object S15_tree=null;
		Object SEMICOLON17_tree=null;
		Object VIEWPORT19_tree=null;
		Object S20_tree=null;
		Object LCURLY21_tree=null;
		Object S22_tree=null;
		Object RCURLY24_tree=null;
		Object FONTFACE25_tree=null;
		Object S26_tree=null;
		Object LCURLY27_tree=null;
		Object S28_tree=null;
		Object RCURLY30_tree=null;
		Object MEDIA31_tree=null;
		Object S32_tree=null;
		Object LCURLY34_tree=null;
		Object S35_tree=null;
		Object S37_tree=null;
		Object RCURLY38_tree=null;
		Object ATKEYWORD39_tree=null;
		Object S40_tree=null;
		Object LCURLY41_tree=null;
		Object RCURLY43_tree=null;
		RewriteRuleTokenStream stream_ATKEYWORD=new RewriteRuleTokenStream(adaptor,"token ATKEYWORD");
		RewriteRuleTokenStream stream_VIEWPORT=new RewriteRuleTokenStream(adaptor,"token VIEWPORT");
		RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
		RewriteRuleTokenStream stream_IMPORT=new RewriteRuleTokenStream(adaptor,"token IMPORT");
		RewriteRuleTokenStream stream_FONTFACE=new RewriteRuleTokenStream(adaptor,"token FONTFACE");
		RewriteRuleTokenStream stream_SEMICOLON=new RewriteRuleTokenStream(adaptor,"token SEMICOLON");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_MEDIA=new RewriteRuleTokenStream(adaptor,"token MEDIA");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleSubtreeStream stream_media_rule=new RewriteRuleSubtreeStream(adaptor,"rule media_rule");
		RewriteRuleSubtreeStream stream_any=new RewriteRuleSubtreeStream(adaptor,"rule any");
		RewriteRuleSubtreeStream stream_import_uri=new RewriteRuleSubtreeStream(adaptor,"rule import_uri");
		RewriteRuleSubtreeStream stream_declarations=new RewriteRuleSubtreeStream(adaptor,"rule declarations");
		RewriteRuleSubtreeStream stream_media=new RewriteRuleSubtreeStream(adaptor,"rule media");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:747:2: ( CHARSET | IMPORT ( S )* import_uri ( S )* ( media )? SEMICOLON -> ^( IMPORT ( media )? import_uri ) | page | VIEWPORT ( S )* LCURLY ( S )* declarations RCURLY -> ^( VIEWPORT declarations ) | FONTFACE ( S )* LCURLY ( S )* declarations RCURLY -> ^( FONTFACE declarations ) | MEDIA ( S )* ( media )? LCURLY ( S )* ( media_rule ( S )* )* RCURLY -> ^( MEDIA ( media )? ( media_rule )* ) | ATKEYWORD ( S )* LCURLY ( any )* RCURLY -> INVALID_STATEMENT )
			int alt20=7;
			switch ( input.LA(1) ) {
			case CHARSET:
				{
				alt20=1;
				}
				break;
			case IMPORT:
				{
				alt20=2;
				}
				break;
			case PAGE:
				{
				alt20=3;
				}
				break;
			case VIEWPORT:
				{
				alt20=4;
				}
				break;
			case FONTFACE:
				{
				alt20=5;
				}
				break;
			case MEDIA:
				{
				alt20=6;
				}
				break;
			case ATKEYWORD:
				{
				alt20=7;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 20, 0, input);
				throw nvae;
			}
			switch (alt20) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:747:4: CHARSET
					{
					root_0 = (Object)adaptor.nil();


					CHARSET11=(Token)match(input,CHARSET,FOLLOW_CHARSET_in_atstatement317); 
					CHARSET11_tree = (Object)adaptor.create(CHARSET11);
					adaptor.addChild(root_0, CHARSET11_tree);

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:748:4: IMPORT ( S )* import_uri ( S )* ( media )? SEMICOLON
					{
					IMPORT12=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_atstatement322);  
					stream_IMPORT.add(IMPORT12);

					// cz/vutbr/web/csskit/antlr/CSS.g:748:11: ( S )*
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( (LA6_0==S) ) {
							alt6=1;
						}

						switch (alt6) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:748:11: S
							{
							S13=(Token)match(input,S,FOLLOW_S_in_atstatement324);  
							stream_S.add(S13);

							}
							break;

						default :
							break loop6;
						}
					}

					pushFollow(FOLLOW_import_uri_in_atstatement327);
					import_uri14=import_uri();
					state._fsp--;

					stream_import_uri.add(import_uri14.getTree());
					// cz/vutbr/web/csskit/antlr/CSS.g:748:25: ( S )*
					loop7:
					while (true) {
						int alt7=2;
						int LA7_0 = input.LA(1);
						if ( (LA7_0==S) ) {
							alt7=1;
						}

						switch (alt7) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:748:25: S
							{
							S15=(Token)match(input,S,FOLLOW_S_in_atstatement329);  
							stream_S.add(S15);

							}
							break;

						default :
							break loop7;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:748:28: ( media )?
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0==ASTERISK||LA8_0==COLON||LA8_0==CTRL||LA8_0==DASHMATCH||LA8_0==DIMENSION||LA8_0==EQUALS||LA8_0==EXCLAMATION||(LA8_0 >= FUNCTION && LA8_0 <= GREATER)||LA8_0==IDENT||LA8_0==INCLUDES||LA8_0==INVALID_STRING||(LA8_0 >= LESS && LA8_0 <= LPAREN)||LA8_0==MINUS||LA8_0==NUMBER||(LA8_0 >= PERCENT && LA8_0 <= PLUS)||LA8_0==QUESTION||LA8_0==RPAREN||LA8_0==SLASH||LA8_0==STRING||(LA8_0 >= UNIRANGE && LA8_0 <= URI)||(LA8_0 >= 101 && LA8_0 <= 103)) ) {
						alt8=1;
					}
					switch (alt8) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:748:28: media
							{
							pushFollow(FOLLOW_media_in_atstatement332);
							media16=media();
							state._fsp--;

							stream_media.add(media16.getTree());
							}
							break;

					}

					SEMICOLON17=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_atstatement335);  
					stream_SEMICOLON.add(SEMICOLON17);

					// AST REWRITE
					// elements: import_uri, media, IMPORT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 749:4: -> ^( IMPORT ( media )? import_uri )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:749:7: ^( IMPORT ( media )? import_uri )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_IMPORT.nextNode(), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:749:16: ( media )?
						if ( stream_media.hasNext() ) {
							adaptor.addChild(root_1, stream_media.nextTree());
						}
						stream_media.reset();

						adaptor.addChild(root_1, stream_import_uri.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:750:4: page
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_page_in_atstatement356);
					page18=page();
					state._fsp--;

					adaptor.addChild(root_0, page18.getTree());

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:751:5: VIEWPORT ( S )* LCURLY ( S )* declarations RCURLY
					{
					VIEWPORT19=(Token)match(input,VIEWPORT,FOLLOW_VIEWPORT_in_atstatement362);  
					stream_VIEWPORT.add(VIEWPORT19);

					// cz/vutbr/web/csskit/antlr/CSS.g:751:14: ( S )*
					loop9:
					while (true) {
						int alt9=2;
						int LA9_0 = input.LA(1);
						if ( (LA9_0==S) ) {
							alt9=1;
						}

						switch (alt9) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:751:14: S
							{
							S20=(Token)match(input,S,FOLLOW_S_in_atstatement364);  
							stream_S.add(S20);

							}
							break;

						default :
							break loop9;
						}
					}

					LCURLY21=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_atstatement371);  
					stream_LCURLY.add(LCURLY21);

					// cz/vutbr/web/csskit/antlr/CSS.g:752:12: ( S )*
					loop10:
					while (true) {
						int alt10=2;
						int LA10_0 = input.LA(1);
						if ( (LA10_0==S) ) {
							alt10=1;
						}

						switch (alt10) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:752:12: S
							{
							S22=(Token)match(input,S,FOLLOW_S_in_atstatement373);  
							stream_S.add(S22);

							}
							break;

						default :
							break loop10;
						}
					}

					pushFollow(FOLLOW_declarations_in_atstatement376);
					declarations23=declarations();
					state._fsp--;

					stream_declarations.add(declarations23.getTree());
					RCURLY24=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_atstatement382);  
					stream_RCURLY.add(RCURLY24);

					// AST REWRITE
					// elements: declarations, VIEWPORT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 753:12: -> ^( VIEWPORT declarations )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:753:15: ^( VIEWPORT declarations )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_VIEWPORT.nextNode(), root_1);
						adaptor.addChild(root_1, stream_declarations.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:754:4: FONTFACE ( S )* LCURLY ( S )* declarations RCURLY
					{
					FONTFACE25=(Token)match(input,FONTFACE,FOLLOW_FONTFACE_in_atstatement395);  
					stream_FONTFACE.add(FONTFACE25);

					// cz/vutbr/web/csskit/antlr/CSS.g:754:13: ( S )*
					loop11:
					while (true) {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( (LA11_0==S) ) {
							alt11=1;
						}

						switch (alt11) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:754:13: S
							{
							S26=(Token)match(input,S,FOLLOW_S_in_atstatement397);  
							stream_S.add(S26);

							}
							break;

						default :
							break loop11;
						}
					}

					LCURLY27=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_atstatement403);  
					stream_LCURLY.add(LCURLY27);

					// cz/vutbr/web/csskit/antlr/CSS.g:755:11: ( S )*
					loop12:
					while (true) {
						int alt12=2;
						int LA12_0 = input.LA(1);
						if ( (LA12_0==S) ) {
							alt12=1;
						}

						switch (alt12) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:755:11: S
							{
							S28=(Token)match(input,S,FOLLOW_S_in_atstatement405);  
							stream_S.add(S28);

							}
							break;

						default :
							break loop12;
						}
					}

					pushFollow(FOLLOW_declarations_in_atstatement408);
					declarations29=declarations();
					state._fsp--;

					stream_declarations.add(declarations29.getTree());
					RCURLY30=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_atstatement413);  
					stream_RCURLY.add(RCURLY30);

					// AST REWRITE
					// elements: declarations, FONTFACE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 756:11: -> ^( FONTFACE declarations )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:756:14: ^( FONTFACE declarations )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_FONTFACE.nextNode(), root_1);
						adaptor.addChild(root_1, stream_declarations.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSS.g:757:4: MEDIA ( S )* ( media )? LCURLY ( S )* ( media_rule ( S )* )* RCURLY
					{
					MEDIA31=(Token)match(input,MEDIA,FOLLOW_MEDIA_in_atstatement426);  
					stream_MEDIA.add(MEDIA31);

					// cz/vutbr/web/csskit/antlr/CSS.g:757:10: ( S )*
					loop13:
					while (true) {
						int alt13=2;
						int LA13_0 = input.LA(1);
						if ( (LA13_0==S) ) {
							alt13=1;
						}

						switch (alt13) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:757:10: S
							{
							S32=(Token)match(input,S,FOLLOW_S_in_atstatement428);  
							stream_S.add(S32);

							}
							break;

						default :
							break loop13;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:757:13: ( media )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==ASTERISK||LA14_0==COLON||LA14_0==CTRL||LA14_0==DASHMATCH||LA14_0==DIMENSION||LA14_0==EQUALS||LA14_0==EXCLAMATION||(LA14_0 >= FUNCTION && LA14_0 <= GREATER)||LA14_0==IDENT||LA14_0==INCLUDES||LA14_0==INVALID_STRING||(LA14_0 >= LESS && LA14_0 <= LPAREN)||LA14_0==MINUS||LA14_0==NUMBER||(LA14_0 >= PERCENT && LA14_0 <= PLUS)||LA14_0==QUESTION||LA14_0==RPAREN||LA14_0==SLASH||LA14_0==STRING||(LA14_0 >= UNIRANGE && LA14_0 <= URI)||(LA14_0 >= 101 && LA14_0 <= 103)) ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:757:13: media
							{
							pushFollow(FOLLOW_media_in_atstatement431);
							media33=media();
							state._fsp--;

							stream_media.add(media33.getTree());
							}
							break;

					}

					LCURLY34=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_atstatement437);  
					stream_LCURLY.add(LCURLY34);

					// cz/vutbr/web/csskit/antlr/CSS.g:758:10: ( S )*
					loop15:
					while (true) {
						int alt15=2;
						int LA15_0 = input.LA(1);
						if ( (LA15_0==S) ) {
							alt15=1;
						}

						switch (alt15) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:758:10: S
							{
							S35=(Token)match(input,S,FOLLOW_S_in_atstatement439);  
							stream_S.add(S35);

							}
							break;

						default :
							break loop15;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:758:13: ( media_rule ( S )* )*
					loop17:
					while (true) {
						int alt17=2;
						int LA17_0 = input.LA(1);
						if ( (LA17_0==ASTERISK||LA17_0==ATKEYWORD||LA17_0==CHARSET||(LA17_0 >= CLASSKEYWORD && LA17_0 <= COMMA)||LA17_0==CTRL||LA17_0==DASHMATCH||LA17_0==DIMENSION||LA17_0==EQUALS||LA17_0==EXCLAMATION||LA17_0==FONTFACE||(LA17_0 >= GREATER && LA17_0 <= IDENT)||LA17_0==IMPORT||LA17_0==INCLUDES||LA17_0==INVALID_SELPART||LA17_0==INVALID_STRING||LA17_0==LBRACE||LA17_0==LESS||LA17_0==MEDIA||LA17_0==MINUS||LA17_0==NUMBER||LA17_0==PAGE||(LA17_0 >= PERCENT && LA17_0 <= PLUS)||LA17_0==QUESTION||LA17_0==RPAREN||LA17_0==SLASH||LA17_0==STRING||(LA17_0 >= UNIRANGE && LA17_0 <= URI)||LA17_0==VIEWPORT||(LA17_0 >= 101 && LA17_0 <= 103)) ) {
							alt17=1;
						}

						switch (alt17) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:758:14: media_rule ( S )*
							{
							pushFollow(FOLLOW_media_rule_in_atstatement443);
							media_rule36=media_rule();
							state._fsp--;

							stream_media_rule.add(media_rule36.getTree());
							// cz/vutbr/web/csskit/antlr/CSS.g:758:25: ( S )*
							loop16:
							while (true) {
								int alt16=2;
								int LA16_0 = input.LA(1);
								if ( (LA16_0==S) ) {
									alt16=1;
								}

								switch (alt16) {
								case 1 :
									// cz/vutbr/web/csskit/antlr/CSS.g:758:25: S
									{
									S37=(Token)match(input,S,FOLLOW_S_in_atstatement445);  
									stream_S.add(S37);

									}
									break;

								default :
									break loop16;
								}
							}

							}
							break;

						default :
							break loop17;
						}
					}

					RCURLY38=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_atstatement450);  
					stream_RCURLY.add(RCURLY38);

					// AST REWRITE
					// elements: media, media_rule, MEDIA
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 758:37: -> ^( MEDIA ( media )? ( media_rule )* )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:758:40: ^( MEDIA ( media )? ( media_rule )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_MEDIA.nextNode(), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:758:48: ( media )?
						if ( stream_media.hasNext() ) {
							adaptor.addChild(root_1, stream_media.nextTree());
						}
						stream_media.reset();

						// cz/vutbr/web/csskit/antlr/CSS.g:758:55: ( media_rule )*
						while ( stream_media_rule.hasNext() ) {
							adaptor.addChild(root_1, stream_media_rule.nextTree());
						}
						stream_media_rule.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSS.g:759:4: ATKEYWORD ( S )* LCURLY ( any )* RCURLY
					{
					ATKEYWORD39=(Token)match(input,ATKEYWORD,FOLLOW_ATKEYWORD_in_atstatement468);  
					stream_ATKEYWORD.add(ATKEYWORD39);

					// cz/vutbr/web/csskit/antlr/CSS.g:759:14: ( S )*
					loop18:
					while (true) {
						int alt18=2;
						int LA18_0 = input.LA(1);
						if ( (LA18_0==S) ) {
							alt18=1;
						}

						switch (alt18) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:759:14: S
							{
							S40=(Token)match(input,S,FOLLOW_S_in_atstatement470);  
							stream_S.add(S40);

							}
							break;

						default :
							break loop18;
						}
					}

					LCURLY41=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_atstatement473);  
					stream_LCURLY.add(LCURLY41);

					// cz/vutbr/web/csskit/antlr/CSS.g:759:24: ( any )*
					loop19:
					while (true) {
						int alt19=2;
						int LA19_0 = input.LA(1);
						if ( (LA19_0==ASTERISK||(LA19_0 >= CLASSKEYWORD && LA19_0 <= COMMA)||LA19_0==DASHMATCH||LA19_0==DIMENSION||LA19_0==EQUALS||LA19_0==EXCLAMATION||(LA19_0 >= FUNCTION && LA19_0 <= IDENT)||LA19_0==INCLUDES||LA19_0==INVALID_STRING||LA19_0==LBRACE||(LA19_0 >= LESS && LA19_0 <= LPAREN)||LA19_0==MINUS||LA19_0==NUMBER||(LA19_0 >= PERCENT && LA19_0 <= PLUS)||LA19_0==QUESTION||LA19_0==SLASH||LA19_0==STRING||(LA19_0 >= UNIRANGE && LA19_0 <= URI)) ) {
							alt19=1;
						}

						switch (alt19) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:759:24: any
							{
							pushFollow(FOLLOW_any_in_atstatement475);
							any42=any();
							state._fsp--;

							stream_any.add(any42.getTree());
							}
							break;

						default :
							break loop19;
						}
					}

					RCURLY43=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_atstatement478);  
					stream_RCURLY.add(RCURLY43);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 759:36: -> INVALID_STATEMENT
					{
						adaptor.addChild(root_0, (Object)adaptor.create(INVALID_STATEMENT, "INVALID_STATEMENT"));
					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			      	final BitSet follow = BitSet.of(CSSLexer.RCURLY, CSSLexer.SEMICOLON);								
				    retval.tree = invalidFallbackGreedy(CSSLexer.INVALID_STATEMENT, 
				  		"INVALID_STATEMENT", follow, re);							
				
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "atstatement"


	public static class import_uri_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "import_uri"
	// cz/vutbr/web/csskit/antlr/CSS.g:767:1: import_uri : ( STRING | URI ) ;
	public final CSSParser.import_uri_return import_uri() throws RecognitionException {
		CSSParser.import_uri_return retval = new CSSParser.import_uri_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set44=null;

		Object set44_tree=null;

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:768:3: ( ( STRING | URI ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:
			{
			root_0 = (Object)adaptor.nil();


			set44=input.LT(1);
			if ( input.LA(1)==STRING||input.LA(1)==URI ) {
				input.consume();
				adaptor.addChild(root_0, (Object)adaptor.create(set44));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "import_uri"


	public static class page_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "page"
	// cz/vutbr/web/csskit/antlr/CSS.g:771:1: page : PAGE ( S )* ( ( IDENT | IDENT page_pseudo | page_pseudo ) ( S )* )? LCURLY ( S )* declarations ( margin_rule )* RCURLY -> ^( PAGE ( IDENT )? ( page_pseudo )? declarations ^( SET ( margin_rule )* ) ) ;
	public final CSSParser.page_return page() throws RecognitionException {
		CSSParser.page_return retval = new CSSParser.page_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PAGE45=null;
		Token S46=null;
		Token IDENT47=null;
		Token IDENT48=null;
		Token S51=null;
		Token LCURLY52=null;
		Token S53=null;
		Token RCURLY56=null;
		ParserRuleReturnScope page_pseudo49 =null;
		ParserRuleReturnScope page_pseudo50 =null;
		ParserRuleReturnScope declarations54 =null;
		ParserRuleReturnScope margin_rule55 =null;

		Object PAGE45_tree=null;
		Object S46_tree=null;
		Object IDENT47_tree=null;
		Object IDENT48_tree=null;
		Object S51_tree=null;
		Object LCURLY52_tree=null;
		Object S53_tree=null;
		Object RCURLY56_tree=null;
		RewriteRuleTokenStream stream_IDENT=new RewriteRuleTokenStream(adaptor,"token IDENT");
		RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
		RewriteRuleTokenStream stream_PAGE=new RewriteRuleTokenStream(adaptor,"token PAGE");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleSubtreeStream stream_page_pseudo=new RewriteRuleSubtreeStream(adaptor,"rule page_pseudo");
		RewriteRuleSubtreeStream stream_declarations=new RewriteRuleSubtreeStream(adaptor,"rule declarations");
		RewriteRuleSubtreeStream stream_margin_rule=new RewriteRuleSubtreeStream(adaptor,"rule margin_rule");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:772:2: ( PAGE ( S )* ( ( IDENT | IDENT page_pseudo | page_pseudo ) ( S )* )? LCURLY ( S )* declarations ( margin_rule )* RCURLY -> ^( PAGE ( IDENT )? ( page_pseudo )? declarations ^( SET ( margin_rule )* ) ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:772:4: PAGE ( S )* ( ( IDENT | IDENT page_pseudo | page_pseudo ) ( S )* )? LCURLY ( S )* declarations ( margin_rule )* RCURLY
			{
			PAGE45=(Token)match(input,PAGE,FOLLOW_PAGE_in_page519);  
			stream_PAGE.add(PAGE45);

			// cz/vutbr/web/csskit/antlr/CSS.g:772:9: ( S )*
			loop21:
			while (true) {
				int alt21=2;
				int LA21_0 = input.LA(1);
				if ( (LA21_0==S) ) {
					alt21=1;
				}

				switch (alt21) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:772:9: S
					{
					S46=(Token)match(input,S,FOLLOW_S_in_page521);  
					stream_S.add(S46);

					}
					break;

				default :
					break loop21;
				}
			}

			// cz/vutbr/web/csskit/antlr/CSS.g:772:12: ( ( IDENT | IDENT page_pseudo | page_pseudo ) ( S )* )?
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==COLON||LA24_0==IDENT) ) {
				alt24=1;
			}
			switch (alt24) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:772:13: ( IDENT | IDENT page_pseudo | page_pseudo ) ( S )*
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:772:13: ( IDENT | IDENT page_pseudo | page_pseudo )
					int alt22=3;
					int LA22_0 = input.LA(1);
					if ( (LA22_0==IDENT) ) {
						int LA22_1 = input.LA(2);
						if ( (LA22_1==LCURLY||LA22_1==S) ) {
							alt22=1;
						}
						else if ( (LA22_1==COLON) ) {
							alt22=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 22, 1, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( (LA22_0==COLON) ) {
						alt22=3;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 22, 0, input);
						throw nvae;
					}

					switch (alt22) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:772:15: IDENT
							{
							IDENT47=(Token)match(input,IDENT,FOLLOW_IDENT_in_page527);  
							stream_IDENT.add(IDENT47);

							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSS.g:772:23: IDENT page_pseudo
							{
							IDENT48=(Token)match(input,IDENT,FOLLOW_IDENT_in_page531);  
							stream_IDENT.add(IDENT48);

							pushFollow(FOLLOW_page_pseudo_in_page533);
							page_pseudo49=page_pseudo();
							state._fsp--;

							stream_page_pseudo.add(page_pseudo49.getTree());
							}
							break;
						case 3 :
							// cz/vutbr/web/csskit/antlr/CSS.g:772:43: page_pseudo
							{
							pushFollow(FOLLOW_page_pseudo_in_page537);
							page_pseudo50=page_pseudo();
							state._fsp--;

							stream_page_pseudo.add(page_pseudo50.getTree());
							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSS.g:772:56: ( S )*
					loop23:
					while (true) {
						int alt23=2;
						int LA23_0 = input.LA(1);
						if ( (LA23_0==S) ) {
							alt23=1;
						}

						switch (alt23) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:772:56: S
							{
							S51=(Token)match(input,S,FOLLOW_S_in_page540);  
							stream_S.add(S51);

							}
							break;

						default :
							break loop23;
						}
					}

					}
					break;

			}

			LCURLY52=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_page548);  
			stream_LCURLY.add(LCURLY52);

			// cz/vutbr/web/csskit/antlr/CSS.g:773:10: ( S )*
			loop25:
			while (true) {
				int alt25=2;
				int LA25_0 = input.LA(1);
				if ( (LA25_0==S) ) {
					alt25=1;
				}

				switch (alt25) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:773:10: S
					{
					S53=(Token)match(input,S,FOLLOW_S_in_page550);  
					stream_S.add(S53);

					}
					break;

				default :
					break loop25;
				}
			}

			pushFollow(FOLLOW_declarations_in_page555);
			declarations54=declarations();
			state._fsp--;

			stream_declarations.add(declarations54.getTree());
			// cz/vutbr/web/csskit/antlr/CSS.g:774:16: ( margin_rule )*
			loop26:
			while (true) {
				int alt26=2;
				int LA26_0 = input.LA(1);
				if ( (LA26_0==MARGIN_AREA) ) {
					alt26=1;
				}

				switch (alt26) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:774:16: margin_rule
					{
					pushFollow(FOLLOW_margin_rule_in_page557);
					margin_rule55=margin_rule();
					state._fsp--;

					stream_margin_rule.add(margin_rule55.getTree());
					}
					break;

				default :
					break loop26;
				}
			}

			RCURLY56=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_page562);  
			stream_RCURLY.add(RCURLY56);

			// AST REWRITE
			// elements: PAGE, page_pseudo, IDENT, margin_rule, declarations
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 776:3: -> ^( PAGE ( IDENT )? ( page_pseudo )? declarations ^( SET ( margin_rule )* ) )
			{
				// cz/vutbr/web/csskit/antlr/CSS.g:776:6: ^( PAGE ( IDENT )? ( page_pseudo )? declarations ^( SET ( margin_rule )* ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(stream_PAGE.nextNode(), root_1);
				// cz/vutbr/web/csskit/antlr/CSS.g:776:13: ( IDENT )?
				if ( stream_IDENT.hasNext() ) {
					adaptor.addChild(root_1, stream_IDENT.nextNode());
				}
				stream_IDENT.reset();

				// cz/vutbr/web/csskit/antlr/CSS.g:776:20: ( page_pseudo )?
				if ( stream_page_pseudo.hasNext() ) {
					adaptor.addChild(root_1, stream_page_pseudo.nextTree());
				}
				stream_page_pseudo.reset();

				adaptor.addChild(root_1, stream_declarations.nextTree());
				// cz/vutbr/web/csskit/antlr/CSS.g:776:46: ^( SET ( margin_rule )* )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(SET, "SET"), root_2);
				// cz/vutbr/web/csskit/antlr/CSS.g:776:52: ( margin_rule )*
				while ( stream_margin_rule.hasNext() ) {
					adaptor.addChild(root_2, stream_margin_rule.nextTree());
				}
				stream_margin_rule.reset();

				adaptor.addChild(root_1, root_2);
				}

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "page"


	public static class page_pseudo_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "page_pseudo"
	// cz/vutbr/web/csskit/antlr/CSS.g:779:1: page_pseudo : pseudocolon ^ IDENT ;
	public final CSSParser.page_pseudo_return page_pseudo() throws RecognitionException {
		CSSParser.page_pseudo_return retval = new CSSParser.page_pseudo_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token IDENT58=null;
		ParserRuleReturnScope pseudocolon57 =null;

		Object IDENT58_tree=null;

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:780:2: ( pseudocolon ^ IDENT )
			// cz/vutbr/web/csskit/antlr/CSS.g:780:4: pseudocolon ^ IDENT
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_pseudocolon_in_page_pseudo596);
			pseudocolon57=pseudocolon();
			state._fsp--;

			root_0 = (Object)adaptor.becomeRoot(pseudocolon57.getTree(), root_0);
			IDENT58=(Token)match(input,IDENT,FOLLOW_IDENT_in_page_pseudo599); 
			IDENT58_tree = (Object)adaptor.create(IDENT58);
			adaptor.addChild(root_0, IDENT58_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "page_pseudo"


	public static class margin_rule_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "margin_rule"
	// cz/vutbr/web/csskit/antlr/CSS.g:783:1: margin_rule : MARGIN_AREA ( S )* LCURLY ( S )* declarations RCURLY ( S )* -> ^( MARGIN_AREA declarations ) ;
	public final CSSParser.margin_rule_return margin_rule() throws RecognitionException {
		CSSParser.margin_rule_return retval = new CSSParser.margin_rule_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token MARGIN_AREA59=null;
		Token S60=null;
		Token LCURLY61=null;
		Token S62=null;
		Token RCURLY64=null;
		Token S65=null;
		ParserRuleReturnScope declarations63 =null;

		Object MARGIN_AREA59_tree=null;
		Object S60_tree=null;
		Object LCURLY61_tree=null;
		Object S62_tree=null;
		Object RCURLY64_tree=null;
		Object S65_tree=null;
		RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleTokenStream stream_MARGIN_AREA=new RewriteRuleTokenStream(adaptor,"token MARGIN_AREA");
		RewriteRuleSubtreeStream stream_declarations=new RewriteRuleSubtreeStream(adaptor,"rule declarations");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:784:2: ( MARGIN_AREA ( S )* LCURLY ( S )* declarations RCURLY ( S )* -> ^( MARGIN_AREA declarations ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:784:4: MARGIN_AREA ( S )* LCURLY ( S )* declarations RCURLY ( S )*
			{
			MARGIN_AREA59=(Token)match(input,MARGIN_AREA,FOLLOW_MARGIN_AREA_in_margin_rule610);  
			stream_MARGIN_AREA.add(MARGIN_AREA59);

			// cz/vutbr/web/csskit/antlr/CSS.g:784:16: ( S )*
			loop27:
			while (true) {
				int alt27=2;
				int LA27_0 = input.LA(1);
				if ( (LA27_0==S) ) {
					alt27=1;
				}

				switch (alt27) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:784:16: S
					{
					S60=(Token)match(input,S,FOLLOW_S_in_margin_rule612);  
					stream_S.add(S60);

					}
					break;

				default :
					break loop27;
				}
			}

			LCURLY61=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_margin_rule615);  
			stream_LCURLY.add(LCURLY61);

			// cz/vutbr/web/csskit/antlr/CSS.g:784:26: ( S )*
			loop28:
			while (true) {
				int alt28=2;
				int LA28_0 = input.LA(1);
				if ( (LA28_0==S) ) {
					alt28=1;
				}

				switch (alt28) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:784:26: S
					{
					S62=(Token)match(input,S,FOLLOW_S_in_margin_rule617);  
					stream_S.add(S62);

					}
					break;

				default :
					break loop28;
				}
			}

			pushFollow(FOLLOW_declarations_in_margin_rule620);
			declarations63=declarations();
			state._fsp--;

			stream_declarations.add(declarations63.getTree());
			RCURLY64=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_margin_rule622);  
			stream_RCURLY.add(RCURLY64);

			// cz/vutbr/web/csskit/antlr/CSS.g:784:49: ( S )*
			loop29:
			while (true) {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( (LA29_0==S) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:784:49: S
					{
					S65=(Token)match(input,S,FOLLOW_S_in_margin_rule624);  
					stream_S.add(S65);

					}
					break;

				default :
					break loop29;
				}
			}

			// AST REWRITE
			// elements: MARGIN_AREA, declarations
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 784:52: -> ^( MARGIN_AREA declarations )
			{
				// cz/vutbr/web/csskit/antlr/CSS.g:784:55: ^( MARGIN_AREA declarations )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(stream_MARGIN_AREA.nextNode(), root_1);
				adaptor.addChild(root_1, stream_declarations.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "margin_rule"


	public static class inlineset_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "inlineset"
	// cz/vutbr/web/csskit/antlr/CSS.g:789:1: inlineset : ( pseudo ( S )* ( COMMA ( S )* pseudo ( S )* )* )? LCURLY declarations RCURLY -> ^( RULE ( pseudo )* declarations ) ;
	public final CSSParser.inlineset_return inlineset() throws RecognitionException {
		CSSParser.inlineset_return retval = new CSSParser.inlineset_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token S67=null;
		Token COMMA68=null;
		Token S69=null;
		Token S71=null;
		Token LCURLY72=null;
		Token RCURLY74=null;
		ParserRuleReturnScope pseudo66 =null;
		ParserRuleReturnScope pseudo70 =null;
		ParserRuleReturnScope declarations73 =null;

		Object S67_tree=null;
		Object COMMA68_tree=null;
		Object S69_tree=null;
		Object S71_tree=null;
		Object LCURLY72_tree=null;
		Object RCURLY74_tree=null;
		RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleSubtreeStream stream_pseudo=new RewriteRuleSubtreeStream(adaptor,"rule pseudo");
		RewriteRuleSubtreeStream stream_declarations=new RewriteRuleSubtreeStream(adaptor,"rule declarations");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:790:2: ( ( pseudo ( S )* ( COMMA ( S )* pseudo ( S )* )* )? LCURLY declarations RCURLY -> ^( RULE ( pseudo )* declarations ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:790:4: ( pseudo ( S )* ( COMMA ( S )* pseudo ( S )* )* )? LCURLY declarations RCURLY
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:790:4: ( pseudo ( S )* ( COMMA ( S )* pseudo ( S )* )* )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==COLON) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:790:5: pseudo ( S )* ( COMMA ( S )* pseudo ( S )* )*
					{
					pushFollow(FOLLOW_pseudo_in_inlineset647);
					pseudo66=pseudo();
					state._fsp--;

					stream_pseudo.add(pseudo66.getTree());
					// cz/vutbr/web/csskit/antlr/CSS.g:790:12: ( S )*
					loop30:
					while (true) {
						int alt30=2;
						int LA30_0 = input.LA(1);
						if ( (LA30_0==S) ) {
							alt30=1;
						}

						switch (alt30) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:790:12: S
							{
							S67=(Token)match(input,S,FOLLOW_S_in_inlineset649);  
							stream_S.add(S67);

							}
							break;

						default :
							break loop30;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:790:15: ( COMMA ( S )* pseudo ( S )* )*
					loop33:
					while (true) {
						int alt33=2;
						int LA33_0 = input.LA(1);
						if ( (LA33_0==COMMA) ) {
							alt33=1;
						}

						switch (alt33) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:790:16: COMMA ( S )* pseudo ( S )*
							{
							COMMA68=(Token)match(input,COMMA,FOLLOW_COMMA_in_inlineset653);  
							stream_COMMA.add(COMMA68);

							// cz/vutbr/web/csskit/antlr/CSS.g:790:22: ( S )*
							loop31:
							while (true) {
								int alt31=2;
								int LA31_0 = input.LA(1);
								if ( (LA31_0==S) ) {
									alt31=1;
								}

								switch (alt31) {
								case 1 :
									// cz/vutbr/web/csskit/antlr/CSS.g:790:22: S
									{
									S69=(Token)match(input,S,FOLLOW_S_in_inlineset655);  
									stream_S.add(S69);

									}
									break;

								default :
									break loop31;
								}
							}

							pushFollow(FOLLOW_pseudo_in_inlineset658);
							pseudo70=pseudo();
							state._fsp--;

							stream_pseudo.add(pseudo70.getTree());
							// cz/vutbr/web/csskit/antlr/CSS.g:790:32: ( S )*
							loop32:
							while (true) {
								int alt32=2;
								int LA32_0 = input.LA(1);
								if ( (LA32_0==S) ) {
									alt32=1;
								}

								switch (alt32) {
								case 1 :
									// cz/vutbr/web/csskit/antlr/CSS.g:790:32: S
									{
									S71=(Token)match(input,S,FOLLOW_S_in_inlineset660);  
									stream_S.add(S71);

									}
									break;

								default :
									break loop32;
								}
							}

							}
							break;

						default :
							break loop33;
						}
					}

					}
					break;

			}

			LCURLY72=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_inlineset673);  
			stream_LCURLY.add(LCURLY72);

			pushFollow(FOLLOW_declarations_in_inlineset679);
			declarations73=declarations();
			state._fsp--;

			stream_declarations.add(declarations73.getTree());
			RCURLY74=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_inlineset684);  
			stream_RCURLY.add(RCURLY74);

			// AST REWRITE
			// elements: declarations, pseudo
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 794:4: -> ^( RULE ( pseudo )* declarations )
			{
				// cz/vutbr/web/csskit/antlr/CSS.g:794:7: ^( RULE ( pseudo )* declarations )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(RULE, "RULE"), root_1);
				// cz/vutbr/web/csskit/antlr/CSS.g:794:14: ( pseudo )*
				while ( stream_pseudo.hasNext() ) {
					adaptor.addChild(root_1, stream_pseudo.nextTree());
				}
				stream_pseudo.reset();

				adaptor.addChild(root_1, stream_declarations.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "inlineset"


	public static class media_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "media"
	// cz/vutbr/web/csskit/antlr/CSS.g:797:1: media : media_query ( COMMA ( S )* media_query )* -> ( ^( MEDIA_QUERY media_query ) )+ ;
	public final CSSParser.media_return media() throws RecognitionException {
		CSSParser.media_return retval = new CSSParser.media_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COMMA76=null;
		Token S77=null;
		ParserRuleReturnScope media_query75 =null;
		ParserRuleReturnScope media_query78 =null;

		Object COMMA76_tree=null;
		Object S77_tree=null;
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleSubtreeStream stream_media_query=new RewriteRuleSubtreeStream(adaptor,"rule media_query");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:798:2: ( media_query ( COMMA ( S )* media_query )* -> ( ^( MEDIA_QUERY media_query ) )+ )
			// cz/vutbr/web/csskit/antlr/CSS.g:798:4: media_query ( COMMA ( S )* media_query )*
			{
			pushFollow(FOLLOW_media_query_in_media711);
			media_query75=media_query();
			state._fsp--;

			stream_media_query.add(media_query75.getTree());
			// cz/vutbr/web/csskit/antlr/CSS.g:798:16: ( COMMA ( S )* media_query )*
			loop36:
			while (true) {
				int alt36=2;
				int LA36_0 = input.LA(1);
				if ( (LA36_0==COMMA) ) {
					alt36=1;
				}

				switch (alt36) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:798:17: COMMA ( S )* media_query
					{
					COMMA76=(Token)match(input,COMMA,FOLLOW_COMMA_in_media714);  
					stream_COMMA.add(COMMA76);

					// cz/vutbr/web/csskit/antlr/CSS.g:798:23: ( S )*
					loop35:
					while (true) {
						int alt35=2;
						int LA35_0 = input.LA(1);
						if ( (LA35_0==S) ) {
							alt35=1;
						}

						switch (alt35) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:798:23: S
							{
							S77=(Token)match(input,S,FOLLOW_S_in_media716);  
							stream_S.add(S77);

							}
							break;

						default :
							break loop35;
						}
					}

					pushFollow(FOLLOW_media_query_in_media719);
					media_query78=media_query();
					state._fsp--;

					stream_media_query.add(media_query78.getTree());
					}
					break;

				default :
					break loop36;
				}
			}

			// AST REWRITE
			// elements: media_query
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 799:5: -> ( ^( MEDIA_QUERY media_query ) )+
			{
				if ( !(stream_media_query.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_media_query.hasNext() ) {
					// cz/vutbr/web/csskit/antlr/CSS.g:799:8: ^( MEDIA_QUERY media_query )
					{
					Object root_1 = (Object)adaptor.nil();
					root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MEDIA_QUERY, "MEDIA_QUERY"), root_1);
					adaptor.addChild(root_1, stream_media_query.nextTree());
					adaptor.addChild(root_0, root_1);
					}

				}
				stream_media_query.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			     final BitSet follow = BitSet.of(CSSLexer.COMMA, CSSLexer.LCURLY, CSSLexer.SEMICOLON);               
			     retval.tree = invalidFallback(CSSLexer.INVALID_STATEMENT, "INVALID_STATEMENT", follow, LexerState.RecoveryMode.BALANCED, null, re);
			 
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "media"


	public static class media_query_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "media_query"
	// cz/vutbr/web/csskit/antlr/CSS.g:806:1: media_query : ( media_term ( S !)* )+ ;
	public final CSSParser.media_query_return media_query() throws RecognitionException {
		CSSParser.media_query_return retval = new CSSParser.media_query_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token S80=null;
		ParserRuleReturnScope media_term79 =null;

		Object S80_tree=null;

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:807:2: ( ( media_term ( S !)* )+ )
			// cz/vutbr/web/csskit/antlr/CSS.g:807:4: ( media_term ( S !)* )+
			{
			root_0 = (Object)adaptor.nil();


			// cz/vutbr/web/csskit/antlr/CSS.g:807:4: ( media_term ( S !)* )+
			int cnt38=0;
			loop38:
			while (true) {
				int alt38=2;
				int LA38_0 = input.LA(1);
				if ( (LA38_0==ASTERISK||LA38_0==COLON||LA38_0==CTRL||LA38_0==DASHMATCH||LA38_0==DIMENSION||LA38_0==EQUALS||LA38_0==EXCLAMATION||(LA38_0 >= FUNCTION && LA38_0 <= GREATER)||LA38_0==IDENT||LA38_0==INCLUDES||LA38_0==INVALID_STRING||(LA38_0 >= LESS && LA38_0 <= LPAREN)||LA38_0==MINUS||LA38_0==NUMBER||(LA38_0 >= PERCENT && LA38_0 <= PLUS)||LA38_0==QUESTION||LA38_0==RPAREN||LA38_0==SLASH||LA38_0==STRING||(LA38_0 >= UNIRANGE && LA38_0 <= URI)||(LA38_0 >= 101 && LA38_0 <= 103)) ) {
					alt38=1;
				}

				switch (alt38) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:807:5: media_term ( S !)*
					{
					pushFollow(FOLLOW_media_term_in_media_query753);
					media_term79=media_term();
					state._fsp--;

					adaptor.addChild(root_0, media_term79.getTree());

					// cz/vutbr/web/csskit/antlr/CSS.g:807:17: ( S !)*
					loop37:
					while (true) {
						int alt37=2;
						int LA37_0 = input.LA(1);
						if ( (LA37_0==S) ) {
							alt37=1;
						}

						switch (alt37) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:807:17: S !
							{
							S80=(Token)match(input,S,FOLLOW_S_in_media_query755); 
							}
							break;

						default :
							break loop37;
						}
					}

					}
					break;

				default :
					if ( cnt38 >= 1 ) break loop38;
					EarlyExitException eee = new EarlyExitException(38, input);
					throw eee;
				}
				cnt38++;
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "media_query"


	public static class media_term_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "media_term"
	// cz/vutbr/web/csskit/antlr/CSS.g:810:1: media_term : ( ( IDENT | media_expression ) | nomediaquery -> INVALID_STATEMENT );
	public final CSSParser.media_term_return media_term() throws RecognitionException {
		CSSParser.media_term_return retval = new CSSParser.media_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token IDENT81=null;
		ParserRuleReturnScope media_expression82 =null;
		ParserRuleReturnScope nomediaquery83 =null;

		Object IDENT81_tree=null;
		RewriteRuleSubtreeStream stream_nomediaquery=new RewriteRuleSubtreeStream(adaptor,"rule nomediaquery");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:811:2: ( ( IDENT | media_expression ) | nomediaquery -> INVALID_STATEMENT )
			int alt40=2;
			int LA40_0 = input.LA(1);
			if ( (LA40_0==IDENT||LA40_0==LPAREN) ) {
				alt40=1;
			}
			else if ( (LA40_0==ASTERISK||LA40_0==COLON||LA40_0==CTRL||LA40_0==DASHMATCH||LA40_0==DIMENSION||LA40_0==EQUALS||LA40_0==EXCLAMATION||(LA40_0 >= FUNCTION && LA40_0 <= GREATER)||LA40_0==INCLUDES||LA40_0==INVALID_STRING||LA40_0==LESS||LA40_0==MINUS||LA40_0==NUMBER||(LA40_0 >= PERCENT && LA40_0 <= PLUS)||LA40_0==QUESTION||LA40_0==RPAREN||LA40_0==SLASH||LA40_0==STRING||(LA40_0 >= UNIRANGE && LA40_0 <= URI)||(LA40_0 >= 101 && LA40_0 <= 103)) ) {
				alt40=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 40, 0, input);
				throw nvae;
			}

			switch (alt40) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:811:4: ( IDENT | media_expression )
					{
					root_0 = (Object)adaptor.nil();


					// cz/vutbr/web/csskit/antlr/CSS.g:811:4: ( IDENT | media_expression )
					int alt39=2;
					int LA39_0 = input.LA(1);
					if ( (LA39_0==IDENT) ) {
						alt39=1;
					}
					else if ( (LA39_0==LPAREN) ) {
						alt39=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 39, 0, input);
						throw nvae;
					}

					switch (alt39) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:811:5: IDENT
							{
							IDENT81=(Token)match(input,IDENT,FOLLOW_IDENT_in_media_term771); 
							IDENT81_tree = (Object)adaptor.create(IDENT81);
							adaptor.addChild(root_0, IDENT81_tree);

							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSS.g:811:13: media_expression
							{
							pushFollow(FOLLOW_media_expression_in_media_term775);
							media_expression82=media_expression();
							state._fsp--;

							adaptor.addChild(root_0, media_expression82.getTree());

							}
							break;

					}

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:812:4: nomediaquery
					{
					pushFollow(FOLLOW_nomediaquery_in_media_term781);
					nomediaquery83=nomediaquery();
					state._fsp--;

					stream_nomediaquery.add(nomediaquery83.getTree());
					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 812:17: -> INVALID_STATEMENT
					{
						adaptor.addChild(root_0, (Object)adaptor.create(INVALID_STATEMENT, "INVALID_STATEMENT"));
					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			     final BitSet follow = BitSet.of(CSSLexer.COMMA, CSSLexer.LCURLY, CSSLexer.SEMICOLON);               
			     retval.tree = invalidFallback(CSSLexer.INVALID_STATEMENT, "INVALID_STATEMENT", follow, LexerState.RecoveryMode.RULE, null, re);
			 
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "media_term"


	public static class media_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "media_expression"
	// cz/vutbr/web/csskit/antlr/CSS.g:819:1: media_expression : LPAREN ( S )* IDENT ( S )* ( COLON ( S )* terms )? RPAREN -> ^( DECLARATION IDENT terms ) ;
	public final CSSParser.media_expression_return media_expression() throws RecognitionException {
		CSSParser.media_expression_return retval = new CSSParser.media_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LPAREN84=null;
		Token S85=null;
		Token IDENT86=null;
		Token S87=null;
		Token COLON88=null;
		Token S89=null;
		Token RPAREN91=null;
		ParserRuleReturnScope terms90 =null;

		Object LPAREN84_tree=null;
		Object S85_tree=null;
		Object IDENT86_tree=null;
		Object S87_tree=null;
		Object COLON88_tree=null;
		Object S89_tree=null;
		Object RPAREN91_tree=null;
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_IDENT=new RewriteRuleTokenStream(adaptor,"token IDENT");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleSubtreeStream stream_terms=new RewriteRuleSubtreeStream(adaptor,"rule terms");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:820:2: ( LPAREN ( S )* IDENT ( S )* ( COLON ( S )* terms )? RPAREN -> ^( DECLARATION IDENT terms ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:820:4: LPAREN ( S )* IDENT ( S )* ( COLON ( S )* terms )? RPAREN
			{
			LPAREN84=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_media_expression803);  
			stream_LPAREN.add(LPAREN84);

			// cz/vutbr/web/csskit/antlr/CSS.g:820:11: ( S )*
			loop41:
			while (true) {
				int alt41=2;
				int LA41_0 = input.LA(1);
				if ( (LA41_0==S) ) {
					alt41=1;
				}

				switch (alt41) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:820:11: S
					{
					S85=(Token)match(input,S,FOLLOW_S_in_media_expression805);  
					stream_S.add(S85);

					}
					break;

				default :
					break loop41;
				}
			}

			IDENT86=(Token)match(input,IDENT,FOLLOW_IDENT_in_media_expression808);  
			stream_IDENT.add(IDENT86);

			// cz/vutbr/web/csskit/antlr/CSS.g:820:20: ( S )*
			loop42:
			while (true) {
				int alt42=2;
				int LA42_0 = input.LA(1);
				if ( (LA42_0==S) ) {
					alt42=1;
				}

				switch (alt42) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:820:20: S
					{
					S87=(Token)match(input,S,FOLLOW_S_in_media_expression810);  
					stream_S.add(S87);

					}
					break;

				default :
					break loop42;
				}
			}

			// cz/vutbr/web/csskit/antlr/CSS.g:820:23: ( COLON ( S )* terms )?
			int alt44=2;
			int LA44_0 = input.LA(1);
			if ( (LA44_0==COLON) ) {
				alt44=1;
			}
			switch (alt44) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:820:24: COLON ( S )* terms
					{
					COLON88=(Token)match(input,COLON,FOLLOW_COLON_in_media_expression814);  
					stream_COLON.add(COLON88);

					// cz/vutbr/web/csskit/antlr/CSS.g:820:30: ( S )*
					loop43:
					while (true) {
						int alt43=2;
						int LA43_0 = input.LA(1);
						if ( (LA43_0==S) ) {
							alt43=1;
						}

						switch (alt43) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:820:30: S
							{
							S89=(Token)match(input,S,FOLLOW_S_in_media_expression816);  
							stream_S.add(S89);

							}
							break;

						default :
							break loop43;
						}
					}

					pushFollow(FOLLOW_terms_in_media_expression819);
					terms90=terms();
					state._fsp--;

					stream_terms.add(terms90.getTree());
					}
					break;

			}

			RPAREN91=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_media_expression823);  
			stream_RPAREN.add(RPAREN91);

			// AST REWRITE
			// elements: IDENT, terms
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 821:5: -> ^( DECLARATION IDENT terms )
			{
				// cz/vutbr/web/csskit/antlr/CSS.g:821:8: ^( DECLARATION IDENT terms )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DECLARATION, "DECLARATION"), root_1);
				adaptor.addChild(root_1, stream_IDENT.nextNode());
				adaptor.addChild(root_1, stream_terms.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

					 final BitSet follow = BitSet.of(CSSLexer.RPAREN, CSSLexer.SEMICOLON);               
					 retval.tree = invalidFallbackGreedy(CSSLexer.INVALID_STATEMENT, 
					   "INVALID_STATEMENT", follow, re);
			 
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "media_expression"


	public static class media_rule_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "media_rule"
	// cz/vutbr/web/csskit/antlr/CSS.g:829:1: media_rule : ( ruleset | atstatement -> INVALID_STATEMENT );
	public final CSSParser.media_rule_return media_rule() throws RecognitionException {
		CSSParser.media_rule_return retval = new CSSParser.media_rule_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope ruleset92 =null;
		ParserRuleReturnScope atstatement93 =null;

		RewriteRuleSubtreeStream stream_atstatement=new RewriteRuleSubtreeStream(adaptor,"rule atstatement");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:830:2: ( ruleset | atstatement -> INVALID_STATEMENT )
			int alt45=2;
			int LA45_0 = input.LA(1);
			if ( (LA45_0==ASTERISK||(LA45_0 >= CLASSKEYWORD && LA45_0 <= COMMA)||LA45_0==CTRL||LA45_0==DASHMATCH||LA45_0==DIMENSION||LA45_0==EQUALS||LA45_0==EXCLAMATION||(LA45_0 >= GREATER && LA45_0 <= IDENT)||LA45_0==INCLUDES||LA45_0==INVALID_SELPART||LA45_0==INVALID_STRING||LA45_0==LBRACE||LA45_0==LESS||LA45_0==MINUS||LA45_0==NUMBER||(LA45_0 >= PERCENT && LA45_0 <= PLUS)||LA45_0==QUESTION||LA45_0==RPAREN||LA45_0==SLASH||LA45_0==STRING||(LA45_0 >= UNIRANGE && LA45_0 <= URI)||(LA45_0 >= 101 && LA45_0 <= 103)) ) {
				alt45=1;
			}
			else if ( (LA45_0==ATKEYWORD||LA45_0==CHARSET||LA45_0==FONTFACE||LA45_0==IMPORT||LA45_0==MEDIA||LA45_0==PAGE||LA45_0==VIEWPORT) ) {
				alt45=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 45, 0, input);
				throw nvae;
			}

			switch (alt45) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:830:4: ruleset
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_ruleset_in_media_rule855);
					ruleset92=ruleset();
					state._fsp--;

					adaptor.addChild(root_0, ruleset92.getTree());

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:831:4: atstatement
					{
					pushFollow(FOLLOW_atstatement_in_media_rule860);
					atstatement93=atstatement();
					state._fsp--;

					stream_atstatement.add(atstatement93.getTree());
					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 831:16: -> INVALID_STATEMENT
					{
						adaptor.addChild(root_0, (Object)adaptor.create(INVALID_STATEMENT, "INVALID_STATEMENT"));
					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "media_rule"


	public static class ruleset_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "ruleset"
	// cz/vutbr/web/csskit/antlr/CSS.g:834:1: ruleset : ( combined_selector ( COMMA ( S )* combined_selector )* LCURLY ( S )* declarations RCURLY -> ^( RULE ( combined_selector )+ declarations ) | norule -> INVALID_STATEMENT );
	public final CSSParser.ruleset_return ruleset() throws RecognitionException {
		CSSParser.ruleset_return retval = new CSSParser.ruleset_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COMMA95=null;
		Token S96=null;
		Token LCURLY98=null;
		Token S99=null;
		Token RCURLY101=null;
		ParserRuleReturnScope combined_selector94 =null;
		ParserRuleReturnScope combined_selector97 =null;
		ParserRuleReturnScope declarations100 =null;
		ParserRuleReturnScope norule102 =null;

		Object COMMA95_tree=null;
		Object S96_tree=null;
		Object LCURLY98_tree=null;
		Object S99_tree=null;
		Object RCURLY101_tree=null;
		RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleSubtreeStream stream_combined_selector=new RewriteRuleSubtreeStream(adaptor,"rule combined_selector");
		RewriteRuleSubtreeStream stream_norule=new RewriteRuleSubtreeStream(adaptor,"rule norule");
		RewriteRuleSubtreeStream stream_declarations=new RewriteRuleSubtreeStream(adaptor,"rule declarations");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:835:2: ( combined_selector ( COMMA ( S )* combined_selector )* LCURLY ( S )* declarations RCURLY -> ^( RULE ( combined_selector )+ declarations ) | norule -> INVALID_STATEMENT )
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==ASTERISK||(LA49_0 >= CLASSKEYWORD && LA49_0 <= COLON)||(LA49_0 >= HASH && LA49_0 <= IDENT)||LA49_0==INVALID_SELPART||LA49_0==LBRACE) ) {
				alt49=1;
			}
			else if ( (LA49_0==COMMA||LA49_0==CTRL||LA49_0==DASHMATCH||LA49_0==DIMENSION||LA49_0==EQUALS||LA49_0==EXCLAMATION||LA49_0==GREATER||LA49_0==INCLUDES||LA49_0==INVALID_STRING||LA49_0==LESS||LA49_0==MINUS||LA49_0==NUMBER||(LA49_0 >= PERCENT && LA49_0 <= PLUS)||LA49_0==QUESTION||LA49_0==RPAREN||LA49_0==SLASH||LA49_0==STRING||(LA49_0 >= UNIRANGE && LA49_0 <= URI)||(LA49_0 >= 101 && LA49_0 <= 103)) ) {
				alt49=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 49, 0, input);
				throw nvae;
			}

			switch (alt49) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:835:4: combined_selector ( COMMA ( S )* combined_selector )* LCURLY ( S )* declarations RCURLY
					{
					pushFollow(FOLLOW_combined_selector_in_ruleset876);
					combined_selector94=combined_selector();
					state._fsp--;

					stream_combined_selector.add(combined_selector94.getTree());
					// cz/vutbr/web/csskit/antlr/CSS.g:835:22: ( COMMA ( S )* combined_selector )*
					loop47:
					while (true) {
						int alt47=2;
						int LA47_0 = input.LA(1);
						if ( (LA47_0==COMMA) ) {
							alt47=1;
						}

						switch (alt47) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:835:23: COMMA ( S )* combined_selector
							{
							COMMA95=(Token)match(input,COMMA,FOLLOW_COMMA_in_ruleset879);  
							stream_COMMA.add(COMMA95);

							// cz/vutbr/web/csskit/antlr/CSS.g:835:29: ( S )*
							loop46:
							while (true) {
								int alt46=2;
								int LA46_0 = input.LA(1);
								if ( (LA46_0==S) ) {
									alt46=1;
								}

								switch (alt46) {
								case 1 :
									// cz/vutbr/web/csskit/antlr/CSS.g:835:29: S
									{
									S96=(Token)match(input,S,FOLLOW_S_in_ruleset881);  
									stream_S.add(S96);

									}
									break;

								default :
									break loop46;
								}
							}

							pushFollow(FOLLOW_combined_selector_in_ruleset884);
							combined_selector97=combined_selector();
							state._fsp--;

							stream_combined_selector.add(combined_selector97.getTree());
							}
							break;

						default :
							break loop47;
						}
					}

					LCURLY98=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_ruleset892);  
					stream_LCURLY.add(LCURLY98);

					// cz/vutbr/web/csskit/antlr/CSS.g:836:11: ( S )*
					loop48:
					while (true) {
						int alt48=2;
						int LA48_0 = input.LA(1);
						if ( (LA48_0==S) ) {
							alt48=1;
						}

						switch (alt48) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:836:11: S
							{
							S99=(Token)match(input,S,FOLLOW_S_in_ruleset894);  
							stream_S.add(S99);

							}
							break;

						default :
							break loop48;
						}
					}

					pushFollow(FOLLOW_declarations_in_ruleset902);
					declarations100=declarations();
					state._fsp--;

					stream_declarations.add(declarations100.getTree());
					RCURLY101=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_ruleset907);  
					stream_RCURLY.add(RCURLY101);

					// AST REWRITE
					// elements: combined_selector, declarations
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 839:4: -> ^( RULE ( combined_selector )+ declarations )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:839:7: ^( RULE ( combined_selector )+ declarations )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(RULE, "RULE"), root_1);
						if ( !(stream_combined_selector.hasNext()) ) {
							throw new RewriteEarlyExitException();
						}
						while ( stream_combined_selector.hasNext() ) {
							adaptor.addChild(root_1, stream_combined_selector.nextTree());
						}
						stream_combined_selector.reset();

						adaptor.addChild(root_1, stream_declarations.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:840:4: norule
					{
					pushFollow(FOLLOW_norule_in_ruleset926);
					norule102=norule();
					state._fsp--;

					stream_norule.add(norule102.getTree());
					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 840:11: -> INVALID_STATEMENT
					{
						adaptor.addChild(root_0, (Object)adaptor.create(INVALID_STATEMENT, "INVALID_STATEMENT"));
					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			      final BitSet follow = BitSet.of(CSSLexer.RCURLY);
			      //we don't require {} to be balanced here because of possible parent 'media' sections that may remain open => RecoveryMode.RULE
				    retval.tree = invalidFallbackGreedy(CSSLexer.INVALID_STATEMENT,	"INVALID_STATEMENT", follow, LexerState.RecoveryMode.RULE, null, re);							
				
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "ruleset"


	public static class declarations_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "declarations"
	// cz/vutbr/web/csskit/antlr/CSS.g:848:1: declarations : ( declaration )? ( SEMICOLON ( S )* ( declaration )? )* -> ^( SET ( declaration )* ) ;
	public final CSSParser.declarations_return declarations() throws RecognitionException {
		CSSParser.declarations_return retval = new CSSParser.declarations_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token SEMICOLON104=null;
		Token S105=null;
		ParserRuleReturnScope declaration103 =null;
		ParserRuleReturnScope declaration106 =null;

		Object SEMICOLON104_tree=null;
		Object S105_tree=null;
		RewriteRuleTokenStream stream_SEMICOLON=new RewriteRuleTokenStream(adaptor,"token SEMICOLON");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleSubtreeStream stream_declaration=new RewriteRuleSubtreeStream(adaptor,"rule declaration");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:849:2: ( ( declaration )? ( SEMICOLON ( S )* ( declaration )? )* -> ^( SET ( declaration )* ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:849:4: ( declaration )? ( SEMICOLON ( S )* ( declaration )? )*
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:849:4: ( declaration )?
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==ASTERISK||(LA50_0 >= CLASSKEYWORD && LA50_0 <= COMMA)||LA50_0==CTRL||LA50_0==DASHMATCH||LA50_0==EQUALS||LA50_0==EXCLAMATION||LA50_0==GREATER||LA50_0==IDENT||LA50_0==INCLUDES||LA50_0==INVALID_TOKEN||LA50_0==LESS||LA50_0==MINUS||LA50_0==NUMBER||LA50_0==PERCENT||LA50_0==PLUS||LA50_0==QUESTION||LA50_0==SLASH||LA50_0==STRING_CHAR) ) {
				alt50=1;
			}
			switch (alt50) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:849:4: declaration
					{
					pushFollow(FOLLOW_declaration_in_declarations948);
					declaration103=declaration();
					state._fsp--;

					stream_declaration.add(declaration103.getTree());
					}
					break;

			}

			// cz/vutbr/web/csskit/antlr/CSS.g:849:17: ( SEMICOLON ( S )* ( declaration )? )*
			loop53:
			while (true) {
				int alt53=2;
				int LA53_0 = input.LA(1);
				if ( (LA53_0==SEMICOLON) ) {
					alt53=1;
				}

				switch (alt53) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:849:18: SEMICOLON ( S )* ( declaration )?
					{
					SEMICOLON104=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_declarations952);  
					stream_SEMICOLON.add(SEMICOLON104);

					// cz/vutbr/web/csskit/antlr/CSS.g:849:28: ( S )*
					loop51:
					while (true) {
						int alt51=2;
						int LA51_0 = input.LA(1);
						if ( (LA51_0==S) ) {
							alt51=1;
						}

						switch (alt51) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:849:28: S
							{
							S105=(Token)match(input,S,FOLLOW_S_in_declarations954);  
							stream_S.add(S105);

							}
							break;

						default :
							break loop51;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:849:31: ( declaration )?
					int alt52=2;
					int LA52_0 = input.LA(1);
					if ( (LA52_0==ASTERISK||(LA52_0 >= CLASSKEYWORD && LA52_0 <= COMMA)||LA52_0==CTRL||LA52_0==DASHMATCH||LA52_0==EQUALS||LA52_0==EXCLAMATION||LA52_0==GREATER||LA52_0==IDENT||LA52_0==INCLUDES||LA52_0==INVALID_TOKEN||LA52_0==LESS||LA52_0==MINUS||LA52_0==NUMBER||LA52_0==PERCENT||LA52_0==PLUS||LA52_0==QUESTION||LA52_0==SLASH||LA52_0==STRING_CHAR) ) {
						alt52=1;
					}
					switch (alt52) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:849:31: declaration
							{
							pushFollow(FOLLOW_declaration_in_declarations957);
							declaration106=declaration();
							state._fsp--;

							stream_declaration.add(declaration106.getTree());
							}
							break;

					}

					}
					break;

				default :
					break loop53;
				}
			}

			// AST REWRITE
			// elements: declaration
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 850:4: -> ^( SET ( declaration )* )
			{
				// cz/vutbr/web/csskit/antlr/CSS.g:850:7: ^( SET ( declaration )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SET, "SET"), root_1);
				// cz/vutbr/web/csskit/antlr/CSS.g:850:13: ( declaration )*
				while ( stream_declaration.hasNext() ) {
					adaptor.addChild(root_1, stream_declaration.nextTree());
				}
				stream_declaration.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "declarations"


	public static class declaration_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "declaration"
	// cz/vutbr/web/csskit/antlr/CSS.g:853:1: declaration : ( property COLON ( S )* ( terms )? ( important )? -> ^( DECLARATION ( important )? property ( terms )? ) | noprop ( any )* -> INVALID_DECLARATION );
	public final CSSParser.declaration_return declaration() throws RecognitionException {
		CSSParser.declaration_return retval = new CSSParser.declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COLON108=null;
		Token S109=null;
		ParserRuleReturnScope property107 =null;
		ParserRuleReturnScope terms110 =null;
		ParserRuleReturnScope important111 =null;
		ParserRuleReturnScope noprop112 =null;
		ParserRuleReturnScope any113 =null;

		Object COLON108_tree=null;
		Object S109_tree=null;
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleSubtreeStream stream_important=new RewriteRuleSubtreeStream(adaptor,"rule important");
		RewriteRuleSubtreeStream stream_any=new RewriteRuleSubtreeStream(adaptor,"rule any");
		RewriteRuleSubtreeStream stream_terms=new RewriteRuleSubtreeStream(adaptor,"rule terms");
		RewriteRuleSubtreeStream stream_property=new RewriteRuleSubtreeStream(adaptor,"rule property");
		RewriteRuleSubtreeStream stream_noprop=new RewriteRuleSubtreeStream(adaptor,"rule noprop");


		  LexerState begin = getCurrentLexerState(retval.start);
		  log.trace("Decl begin: " + begin);

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:858:2: ( property COLON ( S )* ( terms )? ( important )? -> ^( DECLARATION ( important )? property ( terms )? ) | noprop ( any )* -> INVALID_DECLARATION )
			int alt58=2;
			int LA58_0 = input.LA(1);
			if ( (LA58_0==IDENT||LA58_0==MINUS) ) {
				alt58=1;
			}
			else if ( (LA58_0==ASTERISK||(LA58_0 >= CLASSKEYWORD && LA58_0 <= COMMA)||LA58_0==CTRL||LA58_0==DASHMATCH||LA58_0==EQUALS||LA58_0==EXCLAMATION||LA58_0==GREATER||LA58_0==INCLUDES||LA58_0==INVALID_TOKEN||LA58_0==LESS||LA58_0==NUMBER||LA58_0==PERCENT||LA58_0==PLUS||LA58_0==QUESTION||LA58_0==SLASH||LA58_0==STRING_CHAR) ) {
				alt58=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 58, 0, input);
				throw nvae;
			}

			switch (alt58) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:858:4: property COLON ( S )* ( terms )? ( important )?
					{
					pushFollow(FOLLOW_property_in_declaration989);
					property107=property();
					state._fsp--;

					stream_property.add(property107.getTree());
					COLON108=(Token)match(input,COLON,FOLLOW_COLON_in_declaration991);  
					stream_COLON.add(COLON108);

					// cz/vutbr/web/csskit/antlr/CSS.g:858:19: ( S )*
					loop54:
					while (true) {
						int alt54=2;
						int LA54_0 = input.LA(1);
						if ( (LA54_0==S) ) {
							alt54=1;
						}

						switch (alt54) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:858:19: S
							{
							S109=(Token)match(input,S,FOLLOW_S_in_declaration993);  
							stream_S.add(S109);

							}
							break;

						default :
							break loop54;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:858:22: ( terms )?
					int alt55=2;
					int LA55_0 = input.LA(1);
					if ( (LA55_0==ASTERISK||LA55_0==ATKEYWORD||(LA55_0 >= CLASSKEYWORD && LA55_0 <= COMMA)||LA55_0==DASHMATCH||LA55_0==DIMENSION||LA55_0==EQUALS||LA55_0==EXPRESSION||(LA55_0 >= FUNCTION && LA55_0 <= IDENT)||LA55_0==INCLUDES||LA55_0==INVALID_STRING||(LA55_0 >= LBRACE && LA55_0 <= LPAREN)||LA55_0==MINUS||LA55_0==NUMBER||(LA55_0 >= PERCENT && LA55_0 <= PLUS)||LA55_0==QUESTION||LA55_0==SLASH||LA55_0==STRING||(LA55_0 >= UNIRANGE && LA55_0 <= URI)) ) {
						alt55=1;
					}
					switch (alt55) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:858:22: terms
							{
							pushFollow(FOLLOW_terms_in_declaration996);
							terms110=terms();
							state._fsp--;

							stream_terms.add(terms110.getTree());
							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSS.g:858:29: ( important )?
					int alt56=2;
					int LA56_0 = input.LA(1);
					if ( (LA56_0==EXCLAMATION) ) {
						alt56=1;
					}
					switch (alt56) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:858:29: important
							{
							pushFollow(FOLLOW_important_in_declaration999);
							important111=important();
							state._fsp--;

							stream_important.add(important111.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: important, property, terms
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 858:40: -> ^( DECLARATION ( important )? property ( terms )? )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:858:43: ^( DECLARATION ( important )? property ( terms )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DECLARATION, "DECLARATION"), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:858:57: ( important )?
						if ( stream_important.hasNext() ) {
							adaptor.addChild(root_1, stream_important.nextTree());
						}
						stream_important.reset();

						adaptor.addChild(root_1, stream_property.nextTree());
						// cz/vutbr/web/csskit/antlr/CSS.g:858:77: ( terms )?
						if ( stream_terms.hasNext() ) {
							adaptor.addChild(root_1, stream_terms.nextTree());
						}
						stream_terms.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:859:4: noprop ( any )*
					{
					pushFollow(FOLLOW_noprop_in_declaration1019);
					noprop112=noprop();
					state._fsp--;

					stream_noprop.add(noprop112.getTree());
					// cz/vutbr/web/csskit/antlr/CSS.g:859:11: ( any )*
					loop57:
					while (true) {
						int alt57=2;
						int LA57_0 = input.LA(1);
						if ( (LA57_0==ASTERISK||(LA57_0 >= CLASSKEYWORD && LA57_0 <= COMMA)||LA57_0==DASHMATCH||LA57_0==DIMENSION||LA57_0==EQUALS||LA57_0==EXCLAMATION||(LA57_0 >= FUNCTION && LA57_0 <= IDENT)||LA57_0==INCLUDES||LA57_0==INVALID_STRING||LA57_0==LBRACE||(LA57_0 >= LESS && LA57_0 <= LPAREN)||LA57_0==MINUS||LA57_0==NUMBER||(LA57_0 >= PERCENT && LA57_0 <= PLUS)||LA57_0==QUESTION||LA57_0==SLASH||LA57_0==STRING||(LA57_0 >= UNIRANGE && LA57_0 <= URI)) ) {
							alt57=1;
						}

						switch (alt57) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:859:11: any
							{
							pushFollow(FOLLOW_any_in_declaration1021);
							any113=any();
							state._fsp--;

							stream_any.add(any113.getTree());
							}
							break;

						default :
							break loop57;
						}
					}

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 859:16: -> INVALID_DECLARATION
					{
						adaptor.addChild(root_0, (Object)adaptor.create(INVALID_DECLARATION, "INVALID_DECLARATION"));
					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			      final BitSet follow = BitSet.of(CSSLexer.SEMICOLON, CSSLexer.RCURLY); //recover on the declaration end or rule end
			      retval.tree = invalidFallback(CSSLexer.INVALID_DECLARATION, "INVALID_DECLARATION", follow, LexerState.RecoveryMode.DECL, begin, re);             
				
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "declaration"


	public static class important_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "important"
	// cz/vutbr/web/csskit/antlr/CSS.g:866:1: important : EXCLAMATION ( S )* 'important' ( S )* -> IMPORTANT ;
	public final CSSParser.important_return important() throws RecognitionException {
		CSSParser.important_return retval = new CSSParser.important_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EXCLAMATION114=null;
		Token S115=null;
		Token string_literal116=null;
		Token S117=null;

		Object EXCLAMATION114_tree=null;
		Object S115_tree=null;
		Object string_literal116_tree=null;
		Object S117_tree=null;
		RewriteRuleTokenStream stream_EXCLAMATION=new RewriteRuleTokenStream(adaptor,"token EXCLAMATION");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_104=new RewriteRuleTokenStream(adaptor,"token 104");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:867:3: ( EXCLAMATION ( S )* 'important' ( S )* -> IMPORTANT )
			// cz/vutbr/web/csskit/antlr/CSS.g:867:5: EXCLAMATION ( S )* 'important' ( S )*
			{
			EXCLAMATION114=(Token)match(input,EXCLAMATION,FOLLOW_EXCLAMATION_in_important1047);  
			stream_EXCLAMATION.add(EXCLAMATION114);

			// cz/vutbr/web/csskit/antlr/CSS.g:867:17: ( S )*
			loop59:
			while (true) {
				int alt59=2;
				int LA59_0 = input.LA(1);
				if ( (LA59_0==S) ) {
					alt59=1;
				}

				switch (alt59) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:867:17: S
					{
					S115=(Token)match(input,S,FOLLOW_S_in_important1049);  
					stream_S.add(S115);

					}
					break;

				default :
					break loop59;
				}
			}

			string_literal116=(Token)match(input,104,FOLLOW_104_in_important1052);  
			stream_104.add(string_literal116);

			// cz/vutbr/web/csskit/antlr/CSS.g:867:32: ( S )*
			loop60:
			while (true) {
				int alt60=2;
				int LA60_0 = input.LA(1);
				if ( (LA60_0==S) ) {
					alt60=1;
				}

				switch (alt60) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:867:32: S
					{
					S117=(Token)match(input,S,FOLLOW_S_in_important1054);  
					stream_S.add(S117);

					}
					break;

				default :
					break loop60;
				}
			}

			// AST REWRITE
			// elements: 
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 867:35: -> IMPORTANT
			{
				adaptor.addChild(root_0, (Object)adaptor.create(IMPORTANT, "IMPORTANT"));
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			      final BitSet follow = BitSet.of(CSSLexer.RCURLY, CSSLexer.SEMICOLON);               
			      retval.tree = invalidFallback(CSSLexer.INVALID_DIRECTIVE, "INVALID_DIRECTIVE", follow, LexerState.RecoveryMode.RULE, null, re);
			  
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "important"


	public static class property_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "property"
	// cz/vutbr/web/csskit/antlr/CSS.g:874:1: property : ( MINUS )? IDENT ( S )* -> ( MINUS )? IDENT ;
	public final CSSParser.property_return property() throws RecognitionException {
		CSSParser.property_return retval = new CSSParser.property_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token MINUS118=null;
		Token IDENT119=null;
		Token S120=null;

		Object MINUS118_tree=null;
		Object IDENT119_tree=null;
		Object S120_tree=null;
		RewriteRuleTokenStream stream_IDENT=new RewriteRuleTokenStream(adaptor,"token IDENT");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:875:2: ( ( MINUS )? IDENT ( S )* -> ( MINUS )? IDENT )
			// cz/vutbr/web/csskit/antlr/CSS.g:875:4: ( MINUS )? IDENT ( S )*
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:875:4: ( MINUS )?
			int alt61=2;
			int LA61_0 = input.LA(1);
			if ( (LA61_0==MINUS) ) {
				alt61=1;
			}
			switch (alt61) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:875:4: MINUS
					{
					MINUS118=(Token)match(input,MINUS,FOLLOW_MINUS_in_property1083);  
					stream_MINUS.add(MINUS118);

					}
					break;

			}

			IDENT119=(Token)match(input,IDENT,FOLLOW_IDENT_in_property1086);  
			stream_IDENT.add(IDENT119);

			// cz/vutbr/web/csskit/antlr/CSS.g:875:17: ( S )*
			loop62:
			while (true) {
				int alt62=2;
				int LA62_0 = input.LA(1);
				if ( (LA62_0==S) ) {
					alt62=1;
				}

				switch (alt62) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:875:17: S
					{
					S120=(Token)match(input,S,FOLLOW_S_in_property1088);  
					stream_S.add(S120);

					}
					break;

				default :
					break loop62;
				}
			}

			// AST REWRITE
			// elements: MINUS, IDENT
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 875:20: -> ( MINUS )? IDENT
			{
				// cz/vutbr/web/csskit/antlr/CSS.g:875:23: ( MINUS )?
				if ( stream_MINUS.hasNext() ) {
					adaptor.addChild(root_0, stream_MINUS.nextNode());
				}
				stream_MINUS.reset();

				adaptor.addChild(root_0, stream_IDENT.nextNode());
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "property"


	public static class terms_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "terms"
	// cz/vutbr/web/csskit/antlr/CSS.g:878:1: terms : ( term )+ -> ^( VALUE ( term )+ ) ;
	public final CSSParser.terms_return terms() throws RecognitionException {
		CSSParser.terms_return retval = new CSSParser.terms_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope term121 =null;

		RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:879:2: ( ( term )+ -> ^( VALUE ( term )+ ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:879:4: ( term )+
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:879:4: ( term )+
			int cnt63=0;
			loop63:
			while (true) {
				int alt63=2;
				int LA63_0 = input.LA(1);
				if ( (LA63_0==ASTERISK||LA63_0==ATKEYWORD||(LA63_0 >= CLASSKEYWORD && LA63_0 <= COMMA)||LA63_0==DASHMATCH||LA63_0==DIMENSION||LA63_0==EQUALS||LA63_0==EXPRESSION||(LA63_0 >= FUNCTION && LA63_0 <= IDENT)||LA63_0==INCLUDES||LA63_0==INVALID_STRING||(LA63_0 >= LBRACE && LA63_0 <= LPAREN)||LA63_0==MINUS||LA63_0==NUMBER||(LA63_0 >= PERCENT && LA63_0 <= PLUS)||LA63_0==QUESTION||LA63_0==SLASH||LA63_0==STRING||(LA63_0 >= UNIRANGE && LA63_0 <= URI)) ) {
					alt63=1;
				}

				switch (alt63) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:879:4: term
					{
					pushFollow(FOLLOW_term_in_terms1116);
					term121=term();
					state._fsp--;

					stream_term.add(term121.getTree());
					}
					break;

				default :
					if ( cnt63 >= 1 ) break loop63;
					EarlyExitException eee = new EarlyExitException(63, input);
					throw eee;
				}
				cnt63++;
			}

			// AST REWRITE
			// elements: term
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 880:2: -> ^( VALUE ( term )+ )
			{
				// cz/vutbr/web/csskit/antlr/CSS.g:880:5: ^( VALUE ( term )+ )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(VALUE, "VALUE"), root_1);
				if ( !(stream_term.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_term.hasNext() ) {
					adaptor.addChild(root_1, stream_term.nextTree());
				}
				stream_term.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

					if (functLevel == 0)
					{
				      final BitSet follow = BitSet.of(CSSLexer.RCURLY, CSSLexer.SEMICOLON);								
					    retval.tree = invalidFallbackGreedy(CSSLexer.INVALID_STATEMENT, 
					  		"INVALID_STATEMENT", follow, re);
					}
					else
					{
			        final BitSet follow = BitSet.of(CSSLexer.RPAREN, CSSLexer.RCURLY, CSSLexer.SEMICOLON);               
			        retval.tree = invalidFallbackGreedy(CSSLexer.INVALID_STATEMENT, "INVALID_STATEMENT", follow, LexerState.RecoveryMode.FUNCTION, null, re);
					}
				
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "terms"


	public static class term_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "term"
	// cz/vutbr/web/csskit/antlr/CSS.g:896:1: term : ( valuepart -> valuepart | LCURLY ( S )* ( any | SEMICOLON ( S )* )* RCURLY -> CURLYBLOCK | ATKEYWORD ( S )* -> ATKEYWORD );
	public final CSSParser.term_return term() throws RecognitionException {
		CSSParser.term_return retval = new CSSParser.term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LCURLY123=null;
		Token S124=null;
		Token SEMICOLON126=null;
		Token S127=null;
		Token RCURLY128=null;
		Token ATKEYWORD129=null;
		Token S130=null;
		ParserRuleReturnScope valuepart122 =null;
		ParserRuleReturnScope any125 =null;

		Object LCURLY123_tree=null;
		Object S124_tree=null;
		Object SEMICOLON126_tree=null;
		Object S127_tree=null;
		Object RCURLY128_tree=null;
		Object ATKEYWORD129_tree=null;
		Object S130_tree=null;
		RewriteRuleTokenStream stream_ATKEYWORD=new RewriteRuleTokenStream(adaptor,"token ATKEYWORD");
		RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
		RewriteRuleTokenStream stream_SEMICOLON=new RewriteRuleTokenStream(adaptor,"token SEMICOLON");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleSubtreeStream stream_valuepart=new RewriteRuleSubtreeStream(adaptor,"rule valuepart");
		RewriteRuleSubtreeStream stream_any=new RewriteRuleSubtreeStream(adaptor,"rule any");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:897:5: ( valuepart -> valuepart | LCURLY ( S )* ( any | SEMICOLON ( S )* )* RCURLY -> CURLYBLOCK | ATKEYWORD ( S )* -> ATKEYWORD )
			int alt68=3;
			switch ( input.LA(1) ) {
			case ASTERISK:
			case CLASSKEYWORD:
			case COLON:
			case COMMA:
			case DASHMATCH:
			case DIMENSION:
			case EQUALS:
			case EXPRESSION:
			case FUNCTION:
			case GREATER:
			case HASH:
			case IDENT:
			case INCLUDES:
			case INVALID_STRING:
			case LBRACE:
			case LESS:
			case LPAREN:
			case MINUS:
			case NUMBER:
			case PERCENT:
			case PERCENTAGE:
			case PLUS:
			case QUESTION:
			case SLASH:
			case STRING:
			case UNIRANGE:
			case URI:
				{
				alt68=1;
				}
				break;
			case LCURLY:
				{
				alt68=2;
				}
				break;
			case ATKEYWORD:
				{
				alt68=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 68, 0, input);
				throw nvae;
			}
			switch (alt68) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:897:7: valuepart
					{
					pushFollow(FOLLOW_valuepart_in_term1149);
					valuepart122=valuepart();
					state._fsp--;

					stream_valuepart.add(valuepart122.getTree());
					// AST REWRITE
					// elements: valuepart
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 897:17: -> valuepart
					{
						adaptor.addChild(root_0, stream_valuepart.nextTree());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:898:7: LCURLY ( S )* ( any | SEMICOLON ( S )* )* RCURLY
					{
					LCURLY123=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_term1161);  
					stream_LCURLY.add(LCURLY123);

					// cz/vutbr/web/csskit/antlr/CSS.g:898:14: ( S )*
					loop64:
					while (true) {
						int alt64=2;
						int LA64_0 = input.LA(1);
						if ( (LA64_0==S) ) {
							alt64=1;
						}

						switch (alt64) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:898:14: S
							{
							S124=(Token)match(input,S,FOLLOW_S_in_term1163);  
							stream_S.add(S124);

							}
							break;

						default :
							break loop64;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:898:17: ( any | SEMICOLON ( S )* )*
					loop66:
					while (true) {
						int alt66=3;
						int LA66_0 = input.LA(1);
						if ( (LA66_0==ASTERISK||(LA66_0 >= CLASSKEYWORD && LA66_0 <= COMMA)||LA66_0==DASHMATCH||LA66_0==DIMENSION||LA66_0==EQUALS||LA66_0==EXCLAMATION||(LA66_0 >= FUNCTION && LA66_0 <= IDENT)||LA66_0==INCLUDES||LA66_0==INVALID_STRING||LA66_0==LBRACE||(LA66_0 >= LESS && LA66_0 <= LPAREN)||LA66_0==MINUS||LA66_0==NUMBER||(LA66_0 >= PERCENT && LA66_0 <= PLUS)||LA66_0==QUESTION||LA66_0==SLASH||LA66_0==STRING||(LA66_0 >= UNIRANGE && LA66_0 <= URI)) ) {
							alt66=1;
						}
						else if ( (LA66_0==SEMICOLON) ) {
							alt66=2;
						}

						switch (alt66) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:898:18: any
							{
							pushFollow(FOLLOW_any_in_term1167);
							any125=any();
							state._fsp--;

							stream_any.add(any125.getTree());
							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSS.g:898:24: SEMICOLON ( S )*
							{
							SEMICOLON126=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_term1171);  
							stream_SEMICOLON.add(SEMICOLON126);

							// cz/vutbr/web/csskit/antlr/CSS.g:898:34: ( S )*
							loop65:
							while (true) {
								int alt65=2;
								int LA65_0 = input.LA(1);
								if ( (LA65_0==S) ) {
									alt65=1;
								}

								switch (alt65) {
								case 1 :
									// cz/vutbr/web/csskit/antlr/CSS.g:898:34: S
									{
									S127=(Token)match(input,S,FOLLOW_S_in_term1173);  
									stream_S.add(S127);

									}
									break;

								default :
									break loop65;
								}
							}

							}
							break;

						default :
							break loop66;
						}
					}

					RCURLY128=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_term1178);  
					stream_RCURLY.add(RCURLY128);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 898:46: -> CURLYBLOCK
					{
						adaptor.addChild(root_0, (Object)adaptor.create(CURLYBLOCK, "CURLYBLOCK"));
					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:899:7: ATKEYWORD ( S )*
					{
					ATKEYWORD129=(Token)match(input,ATKEYWORD,FOLLOW_ATKEYWORD_in_term1190);  
					stream_ATKEYWORD.add(ATKEYWORD129);

					// cz/vutbr/web/csskit/antlr/CSS.g:899:17: ( S )*
					loop67:
					while (true) {
						int alt67=2;
						int LA67_0 = input.LA(1);
						if ( (LA67_0==S) ) {
							alt67=1;
						}

						switch (alt67) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:899:17: S
							{
							S130=(Token)match(input,S,FOLLOW_S_in_term1192);  
							stream_S.add(S130);

							}
							break;

						default :
							break loop67;
						}
					}

					// AST REWRITE
					// elements: ATKEYWORD
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 899:20: -> ATKEYWORD
					{
						adaptor.addChild(root_0, stream_ATKEYWORD.nextNode());
					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "term"


	public static class funct_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "funct"
	// cz/vutbr/web/csskit/antlr/CSS.g:903:1: funct : ( EXPRESSION -> EXPRESSION | FUNCTION ( S )* ( terms )? RPAREN -> ^( FUNCTION ( terms )? ) );
	public final CSSParser.funct_return funct() throws RecognitionException {
		CSSParser.funct_return retval = new CSSParser.funct_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EXPRESSION131=null;
		Token FUNCTION132=null;
		Token S133=null;
		Token RPAREN135=null;
		ParserRuleReturnScope terms134 =null;

		Object EXPRESSION131_tree=null;
		Object FUNCTION132_tree=null;
		Object S133_tree=null;
		Object RPAREN135_tree=null;
		RewriteRuleTokenStream stream_FUNCTION=new RewriteRuleTokenStream(adaptor,"token FUNCTION");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_EXPRESSION=new RewriteRuleTokenStream(adaptor,"token EXPRESSION");
		RewriteRuleSubtreeStream stream_terms=new RewriteRuleSubtreeStream(adaptor,"rule terms");


			functLevel++;

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:910:3: ( EXPRESSION -> EXPRESSION | FUNCTION ( S )* ( terms )? RPAREN -> ^( FUNCTION ( terms )? ) )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==EXPRESSION) ) {
				alt71=1;
			}
			else if ( (LA71_0==FUNCTION) ) {
				alt71=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 71, 0, input);
				throw nvae;
			}

			switch (alt71) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:910:5: EXPRESSION
					{
					EXPRESSION131=(Token)match(input,EXPRESSION,FOLLOW_EXPRESSION_in_funct1225);  
					stream_EXPRESSION.add(EXPRESSION131);

					// AST REWRITE
					// elements: EXPRESSION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 910:16: -> EXPRESSION
					{
						adaptor.addChild(root_0, stream_EXPRESSION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:911:4: FUNCTION ( S )* ( terms )? RPAREN
					{
					FUNCTION132=(Token)match(input,FUNCTION,FOLLOW_FUNCTION_in_funct1234);  
					stream_FUNCTION.add(FUNCTION132);

					// cz/vutbr/web/csskit/antlr/CSS.g:911:13: ( S )*
					loop69:
					while (true) {
						int alt69=2;
						int LA69_0 = input.LA(1);
						if ( (LA69_0==S) ) {
							alt69=1;
						}

						switch (alt69) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:911:13: S
							{
							S133=(Token)match(input,S,FOLLOW_S_in_funct1236);  
							stream_S.add(S133);

							}
							break;

						default :
							break loop69;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:911:16: ( terms )?
					int alt70=2;
					int LA70_0 = input.LA(1);
					if ( (LA70_0==ASTERISK||LA70_0==ATKEYWORD||(LA70_0 >= CLASSKEYWORD && LA70_0 <= COMMA)||LA70_0==DASHMATCH||LA70_0==DIMENSION||LA70_0==EQUALS||LA70_0==EXPRESSION||(LA70_0 >= FUNCTION && LA70_0 <= IDENT)||LA70_0==INCLUDES||LA70_0==INVALID_STRING||(LA70_0 >= LBRACE && LA70_0 <= LPAREN)||LA70_0==MINUS||LA70_0==NUMBER||(LA70_0 >= PERCENT && LA70_0 <= PLUS)||LA70_0==QUESTION||LA70_0==SLASH||LA70_0==STRING||(LA70_0 >= UNIRANGE && LA70_0 <= URI)) ) {
						alt70=1;
					}
					switch (alt70) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:911:16: terms
							{
							pushFollow(FOLLOW_terms_in_funct1239);
							terms134=terms();
							state._fsp--;

							stream_terms.add(terms134.getTree());
							}
							break;

					}

					RPAREN135=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_funct1242);  
					stream_RPAREN.add(RPAREN135);

					// AST REWRITE
					// elements: terms, FUNCTION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 911:30: -> ^( FUNCTION ( terms )? )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:911:33: ^( FUNCTION ( terms )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_FUNCTION.nextNode(), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:911:44: ( terms )?
						if ( stream_terms.hasNext() ) {
							adaptor.addChild(root_1, stream_terms.nextTree());
						}
						stream_terms.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


				functLevel--;

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "funct"


	public static class valuepart_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "valuepart"
	// cz/vutbr/web/csskit/antlr/CSS.g:915:1: valuepart : ( ( MINUS )? IDENT -> ( MINUS )? IDENT | CLASSKEYWORD -> CLASSKEYWORD | ( MINUS )? NUMBER -> ( MINUS )? NUMBER | ( MINUS )? PERCENTAGE -> ( MINUS )? PERCENTAGE | ( MINUS )? DIMENSION -> ( MINUS )? DIMENSION | string -> string | URI -> URI | HASH -> HASH | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COLON -> COLON | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | PLUS -> PLUS | ASTERISK -> ASTERISK | ( MINUS )? funct -> ( MINUS )? funct | DASHMATCH -> DASHMATCH | LPAREN ( valuepart )* RPAREN -> ^( PARENBLOCK ( valuepart )* ) | LBRACE ( valuepart )* RBRACE -> ^( BRACEBLOCK ( valuepart )* ) ) ! ( S )* ;
	public final CSSParser.valuepart_return valuepart() throws RecognitionException {
		CSSParser.valuepart_return retval = new CSSParser.valuepart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token MINUS136=null;
		Token IDENT137=null;
		Token CLASSKEYWORD138=null;
		Token MINUS139=null;
		Token NUMBER140=null;
		Token MINUS141=null;
		Token PERCENTAGE142=null;
		Token MINUS143=null;
		Token DIMENSION144=null;
		Token URI146=null;
		Token HASH147=null;
		Token UNIRANGE148=null;
		Token INCLUDES149=null;
		Token COLON150=null;
		Token COMMA151=null;
		Token GREATER152=null;
		Token LESS153=null;
		Token QUESTION154=null;
		Token PERCENT155=null;
		Token EQUALS156=null;
		Token SLASH157=null;
		Token PLUS158=null;
		Token ASTERISK159=null;
		Token MINUS160=null;
		Token DASHMATCH162=null;
		Token LPAREN163=null;
		Token RPAREN165=null;
		Token LBRACE166=null;
		Token RBRACE168=null;
		Token S169=null;
		ParserRuleReturnScope string145 =null;
		ParserRuleReturnScope funct161 =null;
		ParserRuleReturnScope valuepart164 =null;
		ParserRuleReturnScope valuepart167 =null;

		Object MINUS136_tree=null;
		Object IDENT137_tree=null;
		Object CLASSKEYWORD138_tree=null;
		Object MINUS139_tree=null;
		Object NUMBER140_tree=null;
		Object MINUS141_tree=null;
		Object PERCENTAGE142_tree=null;
		Object MINUS143_tree=null;
		Object DIMENSION144_tree=null;
		Object URI146_tree=null;
		Object HASH147_tree=null;
		Object UNIRANGE148_tree=null;
		Object INCLUDES149_tree=null;
		Object COLON150_tree=null;
		Object COMMA151_tree=null;
		Object GREATER152_tree=null;
		Object LESS153_tree=null;
		Object QUESTION154_tree=null;
		Object PERCENT155_tree=null;
		Object EQUALS156_tree=null;
		Object SLASH157_tree=null;
		Object PLUS158_tree=null;
		Object ASTERISK159_tree=null;
		Object MINUS160_tree=null;
		Object DASHMATCH162_tree=null;
		Object LPAREN163_tree=null;
		Object RPAREN165_tree=null;
		Object LBRACE166_tree=null;
		Object RBRACE168_tree=null;
		Object S169_tree=null;
		RewriteRuleTokenStream stream_PERCENT=new RewriteRuleTokenStream(adaptor,"token PERCENT");
		RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
		RewriteRuleTokenStream stream_CLASSKEYWORD=new RewriteRuleTokenStream(adaptor,"token CLASSKEYWORD");
		RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_HASH=new RewriteRuleTokenStream(adaptor,"token HASH");
		RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");
		RewriteRuleTokenStream stream_PERCENTAGE=new RewriteRuleTokenStream(adaptor,"token PERCENTAGE");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_ASTERISK=new RewriteRuleTokenStream(adaptor,"token ASTERISK");
		RewriteRuleTokenStream stream_URI=new RewriteRuleTokenStream(adaptor,"token URI");
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_INCLUDES=new RewriteRuleTokenStream(adaptor,"token INCLUDES");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_GREATER=new RewriteRuleTokenStream(adaptor,"token GREATER");
		RewriteRuleTokenStream stream_SLASH=new RewriteRuleTokenStream(adaptor,"token SLASH");
		RewriteRuleTokenStream stream_DASHMATCH=new RewriteRuleTokenStream(adaptor,"token DASHMATCH");
		RewriteRuleTokenStream stream_QUESTION=new RewriteRuleTokenStream(adaptor,"token QUESTION");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_LESS=new RewriteRuleTokenStream(adaptor,"token LESS");
		RewriteRuleTokenStream stream_IDENT=new RewriteRuleTokenStream(adaptor,"token IDENT");
		RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
		RewriteRuleTokenStream stream_DIMENSION=new RewriteRuleTokenStream(adaptor,"token DIMENSION");
		RewriteRuleTokenStream stream_UNIRANGE=new RewriteRuleTokenStream(adaptor,"token UNIRANGE");
		RewriteRuleSubtreeStream stream_valuepart=new RewriteRuleSubtreeStream(adaptor,"rule valuepart");
		RewriteRuleSubtreeStream stream_string=new RewriteRuleSubtreeStream(adaptor,"rule string");
		RewriteRuleSubtreeStream stream_funct=new RewriteRuleSubtreeStream(adaptor,"rule funct");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:916:5: ( ( ( MINUS )? IDENT -> ( MINUS )? IDENT | CLASSKEYWORD -> CLASSKEYWORD | ( MINUS )? NUMBER -> ( MINUS )? NUMBER | ( MINUS )? PERCENTAGE -> ( MINUS )? PERCENTAGE | ( MINUS )? DIMENSION -> ( MINUS )? DIMENSION | string -> string | URI -> URI | HASH -> HASH | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COLON -> COLON | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | PLUS -> PLUS | ASTERISK -> ASTERISK | ( MINUS )? funct -> ( MINUS )? funct | DASHMATCH -> DASHMATCH | LPAREN ( valuepart )* RPAREN -> ^( PARENBLOCK ( valuepart )* ) | LBRACE ( valuepart )* RBRACE -> ^( BRACEBLOCK ( valuepart )* ) ) ! ( S )* )
			// cz/vutbr/web/csskit/antlr/CSS.g:916:7: ( ( MINUS )? IDENT -> ( MINUS )? IDENT | CLASSKEYWORD -> CLASSKEYWORD | ( MINUS )? NUMBER -> ( MINUS )? NUMBER | ( MINUS )? PERCENTAGE -> ( MINUS )? PERCENTAGE | ( MINUS )? DIMENSION -> ( MINUS )? DIMENSION | string -> string | URI -> URI | HASH -> HASH | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COLON -> COLON | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | PLUS -> PLUS | ASTERISK -> ASTERISK | ( MINUS )? funct -> ( MINUS )? funct | DASHMATCH -> DASHMATCH | LPAREN ( valuepart )* RPAREN -> ^( PARENBLOCK ( valuepart )* ) | LBRACE ( valuepart )* RBRACE -> ^( BRACEBLOCK ( valuepart )* ) ) ! ( S )*
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:916:7: ( ( MINUS )? IDENT -> ( MINUS )? IDENT | CLASSKEYWORD -> CLASSKEYWORD | ( MINUS )? NUMBER -> ( MINUS )? NUMBER | ( MINUS )? PERCENTAGE -> ( MINUS )? PERCENTAGE | ( MINUS )? DIMENSION -> ( MINUS )? DIMENSION | string -> string | URI -> URI | HASH -> HASH | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COLON -> COLON | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | PLUS -> PLUS | ASTERISK -> ASTERISK | ( MINUS )? funct -> ( MINUS )? funct | DASHMATCH -> DASHMATCH | LPAREN ( valuepart )* RPAREN -> ^( PARENBLOCK ( valuepart )* ) | LBRACE ( valuepart )* RBRACE -> ^( BRACEBLOCK ( valuepart )* ) )
			int alt79=24;
			switch ( input.LA(1) ) {
			case MINUS:
				{
				switch ( input.LA(2) ) {
				case IDENT:
					{
					alt79=1;
					}
					break;
				case NUMBER:
					{
					alt79=3;
					}
					break;
				case PERCENTAGE:
					{
					alt79=4;
					}
					break;
				case DIMENSION:
					{
					alt79=5;
					}
					break;
				case EXPRESSION:
				case FUNCTION:
					{
					alt79=21;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 79, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case IDENT:
				{
				alt79=1;
				}
				break;
			case CLASSKEYWORD:
				{
				alt79=2;
				}
				break;
			case NUMBER:
				{
				alt79=3;
				}
				break;
			case PERCENTAGE:
				{
				alt79=4;
				}
				break;
			case DIMENSION:
				{
				alt79=5;
				}
				break;
			case INVALID_STRING:
			case STRING:
				{
				alt79=6;
				}
				break;
			case URI:
				{
				alt79=7;
				}
				break;
			case HASH:
				{
				alt79=8;
				}
				break;
			case UNIRANGE:
				{
				alt79=9;
				}
				break;
			case INCLUDES:
				{
				alt79=10;
				}
				break;
			case COLON:
				{
				alt79=11;
				}
				break;
			case COMMA:
				{
				alt79=12;
				}
				break;
			case GREATER:
				{
				alt79=13;
				}
				break;
			case LESS:
				{
				alt79=14;
				}
				break;
			case QUESTION:
				{
				alt79=15;
				}
				break;
			case PERCENT:
				{
				alt79=16;
				}
				break;
			case EQUALS:
				{
				alt79=17;
				}
				break;
			case SLASH:
				{
				alt79=18;
				}
				break;
			case PLUS:
				{
				alt79=19;
				}
				break;
			case ASTERISK:
				{
				alt79=20;
				}
				break;
			case EXPRESSION:
			case FUNCTION:
				{
				alt79=21;
				}
				break;
			case DASHMATCH:
				{
				alt79=22;
				}
				break;
			case LPAREN:
				{
				alt79=23;
				}
				break;
			case LBRACE:
				{
				alt79=24;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 79, 0, input);
				throw nvae;
			}
			switch (alt79) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:916:9: ( MINUS )? IDENT
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:916:9: ( MINUS )?
					int alt72=2;
					int LA72_0 = input.LA(1);
					if ( (LA72_0==MINUS) ) {
						alt72=1;
					}
					switch (alt72) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:916:9: MINUS
							{
							MINUS136=(Token)match(input,MINUS,FOLLOW_MINUS_in_valuepart1269);  
							stream_MINUS.add(MINUS136);

							}
							break;

					}

					IDENT137=(Token)match(input,IDENT,FOLLOW_IDENT_in_valuepart1272);  
					stream_IDENT.add(IDENT137);

					// AST REWRITE
					// elements: MINUS, IDENT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 916:22: -> ( MINUS )? IDENT
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:916:25: ( MINUS )?
						if ( stream_MINUS.hasNext() ) {
							adaptor.addChild(root_0, stream_MINUS.nextNode());
						}
						stream_MINUS.reset();

						adaptor.addChild(root_0, stream_IDENT.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:917:9: CLASSKEYWORD
					{
					CLASSKEYWORD138=(Token)match(input,CLASSKEYWORD,FOLLOW_CLASSKEYWORD_in_valuepart1289);  
					stream_CLASSKEYWORD.add(CLASSKEYWORD138);

					// AST REWRITE
					// elements: CLASSKEYWORD
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 917:22: -> CLASSKEYWORD
					{
						adaptor.addChild(root_0, stream_CLASSKEYWORD.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:918:9: ( MINUS )? NUMBER
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:918:9: ( MINUS )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==MINUS) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:918:9: MINUS
							{
							MINUS139=(Token)match(input,MINUS,FOLLOW_MINUS_in_valuepart1303);  
							stream_MINUS.add(MINUS139);

							}
							break;

					}

					NUMBER140=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_valuepart1306);  
					stream_NUMBER.add(NUMBER140);

					// AST REWRITE
					// elements: NUMBER, MINUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 918:23: -> ( MINUS )? NUMBER
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:918:26: ( MINUS )?
						if ( stream_MINUS.hasNext() ) {
							adaptor.addChild(root_0, stream_MINUS.nextNode());
						}
						stream_MINUS.reset();

						adaptor.addChild(root_0, stream_NUMBER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:919:9: ( MINUS )? PERCENTAGE
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:919:9: ( MINUS )?
					int alt74=2;
					int LA74_0 = input.LA(1);
					if ( (LA74_0==MINUS) ) {
						alt74=1;
					}
					switch (alt74) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:919:9: MINUS
							{
							MINUS141=(Token)match(input,MINUS,FOLLOW_MINUS_in_valuepart1323);  
							stream_MINUS.add(MINUS141);

							}
							break;

					}

					PERCENTAGE142=(Token)match(input,PERCENTAGE,FOLLOW_PERCENTAGE_in_valuepart1326);  
					stream_PERCENTAGE.add(PERCENTAGE142);

					// AST REWRITE
					// elements: PERCENTAGE, MINUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 919:27: -> ( MINUS )? PERCENTAGE
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:919:30: ( MINUS )?
						if ( stream_MINUS.hasNext() ) {
							adaptor.addChild(root_0, stream_MINUS.nextNode());
						}
						stream_MINUS.reset();

						adaptor.addChild(root_0, stream_PERCENTAGE.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:920:9: ( MINUS )? DIMENSION
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:920:9: ( MINUS )?
					int alt75=2;
					int LA75_0 = input.LA(1);
					if ( (LA75_0==MINUS) ) {
						alt75=1;
					}
					switch (alt75) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:920:9: MINUS
							{
							MINUS143=(Token)match(input,MINUS,FOLLOW_MINUS_in_valuepart1343);  
							stream_MINUS.add(MINUS143);

							}
							break;

					}

					DIMENSION144=(Token)match(input,DIMENSION,FOLLOW_DIMENSION_in_valuepart1346);  
					stream_DIMENSION.add(DIMENSION144);

					// AST REWRITE
					// elements: DIMENSION, MINUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 920:26: -> ( MINUS )? DIMENSION
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:920:29: ( MINUS )?
						if ( stream_MINUS.hasNext() ) {
							adaptor.addChild(root_0, stream_MINUS.nextNode());
						}
						stream_MINUS.reset();

						adaptor.addChild(root_0, stream_DIMENSION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSS.g:921:9: string
					{
					pushFollow(FOLLOW_string_in_valuepart1363);
					string145=string();
					state._fsp--;

					stream_string.add(string145.getTree());
					// AST REWRITE
					// elements: string
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 921:16: -> string
					{
						adaptor.addChild(root_0, stream_string.nextTree());
					}


					retval.tree = root_0;

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSS.g:922:9: URI
					{
					URI146=(Token)match(input,URI,FOLLOW_URI_in_valuepart1377);  
					stream_URI.add(URI146);

					// AST REWRITE
					// elements: URI
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 922:16: -> URI
					{
						adaptor.addChild(root_0, stream_URI.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 8 :
					// cz/vutbr/web/csskit/antlr/CSS.g:923:9: HASH
					{
					HASH147=(Token)match(input,HASH,FOLLOW_HASH_in_valuepart1394);  
					stream_HASH.add(HASH147);

					// AST REWRITE
					// elements: HASH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 923:14: -> HASH
					{
						adaptor.addChild(root_0, stream_HASH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 9 :
					// cz/vutbr/web/csskit/antlr/CSS.g:924:9: UNIRANGE
					{
					UNIRANGE148=(Token)match(input,UNIRANGE,FOLLOW_UNIRANGE_in_valuepart1408);  
					stream_UNIRANGE.add(UNIRANGE148);

					// AST REWRITE
					// elements: UNIRANGE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 924:18: -> UNIRANGE
					{
						adaptor.addChild(root_0, stream_UNIRANGE.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 10 :
					// cz/vutbr/web/csskit/antlr/CSS.g:925:9: INCLUDES
					{
					INCLUDES149=(Token)match(input,INCLUDES,FOLLOW_INCLUDES_in_valuepart1422);  
					stream_INCLUDES.add(INCLUDES149);

					// AST REWRITE
					// elements: INCLUDES
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 925:18: -> INCLUDES
					{
						adaptor.addChild(root_0, stream_INCLUDES.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 11 :
					// cz/vutbr/web/csskit/antlr/CSS.g:926:9: COLON
					{
					COLON150=(Token)match(input,COLON,FOLLOW_COLON_in_valuepart1436);  
					stream_COLON.add(COLON150);

					// AST REWRITE
					// elements: COLON
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 926:15: -> COLON
					{
						adaptor.addChild(root_0, stream_COLON.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 12 :
					// cz/vutbr/web/csskit/antlr/CSS.g:927:9: COMMA
					{
					COMMA151=(Token)match(input,COMMA,FOLLOW_COMMA_in_valuepart1450);  
					stream_COMMA.add(COMMA151);

					// AST REWRITE
					// elements: COMMA
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 927:15: -> COMMA
					{
						adaptor.addChild(root_0, stream_COMMA.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 13 :
					// cz/vutbr/web/csskit/antlr/CSS.g:928:9: GREATER
					{
					GREATER152=(Token)match(input,GREATER,FOLLOW_GREATER_in_valuepart1464);  
					stream_GREATER.add(GREATER152);

					// AST REWRITE
					// elements: GREATER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 928:17: -> GREATER
					{
						adaptor.addChild(root_0, stream_GREATER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 14 :
					// cz/vutbr/web/csskit/antlr/CSS.g:929:9: LESS
					{
					LESS153=(Token)match(input,LESS,FOLLOW_LESS_in_valuepart1478);  
					stream_LESS.add(LESS153);

					// AST REWRITE
					// elements: LESS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 929:14: -> LESS
					{
						adaptor.addChild(root_0, stream_LESS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 15 :
					// cz/vutbr/web/csskit/antlr/CSS.g:930:9: QUESTION
					{
					QUESTION154=(Token)match(input,QUESTION,FOLLOW_QUESTION_in_valuepart1492);  
					stream_QUESTION.add(QUESTION154);

					// AST REWRITE
					// elements: QUESTION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 930:18: -> QUESTION
					{
						adaptor.addChild(root_0, stream_QUESTION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 16 :
					// cz/vutbr/web/csskit/antlr/CSS.g:931:9: PERCENT
					{
					PERCENT155=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_valuepart1506);  
					stream_PERCENT.add(PERCENT155);

					// AST REWRITE
					// elements: PERCENT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 931:17: -> PERCENT
					{
						adaptor.addChild(root_0, stream_PERCENT.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 17 :
					// cz/vutbr/web/csskit/antlr/CSS.g:932:9: EQUALS
					{
					EQUALS156=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_valuepart1520);  
					stream_EQUALS.add(EQUALS156);

					// AST REWRITE
					// elements: EQUALS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 932:16: -> EQUALS
					{
						adaptor.addChild(root_0, stream_EQUALS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 18 :
					// cz/vutbr/web/csskit/antlr/CSS.g:933:9: SLASH
					{
					SLASH157=(Token)match(input,SLASH,FOLLOW_SLASH_in_valuepart1534);  
					stream_SLASH.add(SLASH157);

					// AST REWRITE
					// elements: SLASH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 933:15: -> SLASH
					{
						adaptor.addChild(root_0, stream_SLASH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 19 :
					// cz/vutbr/web/csskit/antlr/CSS.g:934:8: PLUS
					{
					PLUS158=(Token)match(input,PLUS,FOLLOW_PLUS_in_valuepart1547);  
					stream_PLUS.add(PLUS158);

					// AST REWRITE
					// elements: PLUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 934:13: -> PLUS
					{
						adaptor.addChild(root_0, stream_PLUS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 20 :
					// cz/vutbr/web/csskit/antlr/CSS.g:935:8: ASTERISK
					{
					ASTERISK159=(Token)match(input,ASTERISK,FOLLOW_ASTERISK_in_valuepart1560);  
					stream_ASTERISK.add(ASTERISK159);

					// AST REWRITE
					// elements: ASTERISK
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 935:17: -> ASTERISK
					{
						adaptor.addChild(root_0, stream_ASTERISK.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 21 :
					// cz/vutbr/web/csskit/antlr/CSS.g:936:9: ( MINUS )? funct
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:936:9: ( MINUS )?
					int alt76=2;
					int LA76_0 = input.LA(1);
					if ( (LA76_0==MINUS) ) {
						alt76=1;
					}
					switch (alt76) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:936:9: MINUS
							{
							MINUS160=(Token)match(input,MINUS,FOLLOW_MINUS_in_valuepart1577);  
							stream_MINUS.add(MINUS160);

							}
							break;

					}

					pushFollow(FOLLOW_funct_in_valuepart1580);
					funct161=funct();
					state._fsp--;

					stream_funct.add(funct161.getTree());
					// AST REWRITE
					// elements: funct, MINUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 936:22: -> ( MINUS )? funct
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:936:25: ( MINUS )?
						if ( stream_MINUS.hasNext() ) {
							adaptor.addChild(root_0, stream_MINUS.nextNode());
						}
						stream_MINUS.reset();

						adaptor.addChild(root_0, stream_funct.nextTree());
					}


					retval.tree = root_0;

					}
					break;
				case 22 :
					// cz/vutbr/web/csskit/antlr/CSS.g:937:9: DASHMATCH
					{
					DASHMATCH162=(Token)match(input,DASHMATCH,FOLLOW_DASHMATCH_in_valuepart1598);  
					stream_DASHMATCH.add(DASHMATCH162);

					// AST REWRITE
					// elements: DASHMATCH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 937:19: -> DASHMATCH
					{
						adaptor.addChild(root_0, stream_DASHMATCH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 23 :
					// cz/vutbr/web/csskit/antlr/CSS.g:938:9: LPAREN ( valuepart )* RPAREN
					{
					LPAREN163=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_valuepart1612);  
					stream_LPAREN.add(LPAREN163);

					// cz/vutbr/web/csskit/antlr/CSS.g:938:16: ( valuepart )*
					loop77:
					while (true) {
						int alt77=2;
						int LA77_0 = input.LA(1);
						if ( (LA77_0==ASTERISK||(LA77_0 >= CLASSKEYWORD && LA77_0 <= COMMA)||LA77_0==DASHMATCH||LA77_0==DIMENSION||LA77_0==EQUALS||LA77_0==EXPRESSION||(LA77_0 >= FUNCTION && LA77_0 <= IDENT)||LA77_0==INCLUDES||LA77_0==INVALID_STRING||LA77_0==LBRACE||(LA77_0 >= LESS && LA77_0 <= LPAREN)||LA77_0==MINUS||LA77_0==NUMBER||(LA77_0 >= PERCENT && LA77_0 <= PLUS)||LA77_0==QUESTION||LA77_0==SLASH||LA77_0==STRING||(LA77_0 >= UNIRANGE && LA77_0 <= URI)) ) {
							alt77=1;
						}

						switch (alt77) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:938:16: valuepart
							{
							pushFollow(FOLLOW_valuepart_in_valuepart1614);
							valuepart164=valuepart();
							state._fsp--;

							stream_valuepart.add(valuepart164.getTree());
							}
							break;

						default :
							break loop77;
						}
					}

					RPAREN165=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_valuepart1617);  
					stream_RPAREN.add(RPAREN165);

					// AST REWRITE
					// elements: valuepart
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 938:34: -> ^( PARENBLOCK ( valuepart )* )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:938:37: ^( PARENBLOCK ( valuepart )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PARENBLOCK, "PARENBLOCK"), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:938:50: ( valuepart )*
						while ( stream_valuepart.hasNext() ) {
							adaptor.addChild(root_1, stream_valuepart.nextTree());
						}
						stream_valuepart.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 24 :
					// cz/vutbr/web/csskit/antlr/CSS.g:939:9: LBRACE ( valuepart )* RBRACE
					{
					LBRACE166=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_valuepart1636);  
					stream_LBRACE.add(LBRACE166);

					// cz/vutbr/web/csskit/antlr/CSS.g:939:16: ( valuepart )*
					loop78:
					while (true) {
						int alt78=2;
						int LA78_0 = input.LA(1);
						if ( (LA78_0==ASTERISK||(LA78_0 >= CLASSKEYWORD && LA78_0 <= COMMA)||LA78_0==DASHMATCH||LA78_0==DIMENSION||LA78_0==EQUALS||LA78_0==EXPRESSION||(LA78_0 >= FUNCTION && LA78_0 <= IDENT)||LA78_0==INCLUDES||LA78_0==INVALID_STRING||LA78_0==LBRACE||(LA78_0 >= LESS && LA78_0 <= LPAREN)||LA78_0==MINUS||LA78_0==NUMBER||(LA78_0 >= PERCENT && LA78_0 <= PLUS)||LA78_0==QUESTION||LA78_0==SLASH||LA78_0==STRING||(LA78_0 >= UNIRANGE && LA78_0 <= URI)) ) {
							alt78=1;
						}

						switch (alt78) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:939:16: valuepart
							{
							pushFollow(FOLLOW_valuepart_in_valuepart1638);
							valuepart167=valuepart();
							state._fsp--;

							stream_valuepart.add(valuepart167.getTree());
							}
							break;

						default :
							break loop78;
						}
					}

					RBRACE168=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_valuepart1641);  
					stream_RBRACE.add(RBRACE168);

					// AST REWRITE
					// elements: valuepart
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 939:34: -> ^( BRACEBLOCK ( valuepart )* )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:939:37: ^( BRACEBLOCK ( valuepart )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BRACEBLOCK, "BRACEBLOCK"), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:939:50: ( valuepart )*
						while ( stream_valuepart.hasNext() ) {
							adaptor.addChild(root_1, stream_valuepart.nextTree());
						}
						stream_valuepart.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;

			}

			// cz/vutbr/web/csskit/antlr/CSS.g:940:8: ( S )*
			loop80:
			while (true) {
				int alt80=2;
				int LA80_0 = input.LA(1);
				if ( (LA80_0==S) ) {
					alt80=1;
				}

				switch (alt80) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:940:8: S
					{
					S169=(Token)match(input,S,FOLLOW_S_in_valuepart1659);  
					stream_S.add(S169);

					}
					break;

				default :
					break loop80;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "valuepart"


	public static class combined_selector_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "combined_selector"
	// cz/vutbr/web/csskit/antlr/CSS.g:943:1: combined_selector : selector ( ( combinator ) selector )* ;
	public final CSSParser.combined_selector_return combined_selector() throws RecognitionException {
		CSSParser.combined_selector_return retval = new CSSParser.combined_selector_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope selector170 =null;
		ParserRuleReturnScope combinator171 =null;
		ParserRuleReturnScope selector172 =null;


		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:944:2: ( selector ( ( combinator ) selector )* )
			// cz/vutbr/web/csskit/antlr/CSS.g:944:4: selector ( ( combinator ) selector )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_selector_in_combined_selector1676);
			selector170=selector();
			state._fsp--;

			adaptor.addChild(root_0, selector170.getTree());

			// cz/vutbr/web/csskit/antlr/CSS.g:944:13: ( ( combinator ) selector )*
			loop81:
			while (true) {
				int alt81=2;
				int LA81_0 = input.LA(1);
				if ( (LA81_0==GREATER||LA81_0==PLUS||LA81_0==S||LA81_0==TILDE) ) {
					alt81=1;
				}

				switch (alt81) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:944:14: ( combinator ) selector
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:944:14: ( combinator )
					// cz/vutbr/web/csskit/antlr/CSS.g:944:15: combinator
					{
					pushFollow(FOLLOW_combinator_in_combined_selector1680);
					combinator171=combinator();
					state._fsp--;

					adaptor.addChild(root_0, combinator171.getTree());

					}

					pushFollow(FOLLOW_selector_in_combined_selector1683);
					selector172=selector();
					state._fsp--;

					adaptor.addChild(root_0, selector172.getTree());

					}
					break;

				default :
					break loop81;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

				  log.warn("INVALID COMBINED SELECTOR");
				  reportError(re);
			      recover(input,re);
				
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "combined_selector"


	public static class combinator_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "combinator"
	// cz/vutbr/web/csskit/antlr/CSS.g:952:1: combinator : ( GREATER ( S )* -> CHILD | PLUS ( S )* -> ADJACENT | TILDE ( S )* -> PRECEDING | S -> DESCENDANT );
	public final CSSParser.combinator_return combinator() throws RecognitionException {
		CSSParser.combinator_return retval = new CSSParser.combinator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token GREATER173=null;
		Token S174=null;
		Token PLUS175=null;
		Token S176=null;
		Token TILDE177=null;
		Token S178=null;
		Token S179=null;

		Object GREATER173_tree=null;
		Object S174_tree=null;
		Object PLUS175_tree=null;
		Object S176_tree=null;
		Object TILDE177_tree=null;
		Object S178_tree=null;
		Object S179_tree=null;
		RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
		RewriteRuleTokenStream stream_GREATER=new RewriteRuleTokenStream(adaptor,"token GREATER");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:953:2: ( GREATER ( S )* -> CHILD | PLUS ( S )* -> ADJACENT | TILDE ( S )* -> PRECEDING | S -> DESCENDANT )
			int alt85=4;
			switch ( input.LA(1) ) {
			case GREATER:
				{
				alt85=1;
				}
				break;
			case PLUS:
				{
				alt85=2;
				}
				break;
			case TILDE:
				{
				alt85=3;
				}
				break;
			case S:
				{
				alt85=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 85, 0, input);
				throw nvae;
			}
			switch (alt85) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:953:4: GREATER ( S )*
					{
					GREATER173=(Token)match(input,GREATER,FOLLOW_GREATER_in_combinator1703);  
					stream_GREATER.add(GREATER173);

					// cz/vutbr/web/csskit/antlr/CSS.g:953:12: ( S )*
					loop82:
					while (true) {
						int alt82=2;
						int LA82_0 = input.LA(1);
						if ( (LA82_0==S) ) {
							alt82=1;
						}

						switch (alt82) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:953:12: S
							{
							S174=(Token)match(input,S,FOLLOW_S_in_combinator1705);  
							stream_S.add(S174);

							}
							break;

						default :
							break loop82;
						}
					}

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 953:15: -> CHILD
					{
						adaptor.addChild(root_0, (Object)adaptor.create(CHILD, "CHILD"));
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:954:4: PLUS ( S )*
					{
					PLUS175=(Token)match(input,PLUS,FOLLOW_PLUS_in_combinator1715);  
					stream_PLUS.add(PLUS175);

					// cz/vutbr/web/csskit/antlr/CSS.g:954:9: ( S )*
					loop83:
					while (true) {
						int alt83=2;
						int LA83_0 = input.LA(1);
						if ( (LA83_0==S) ) {
							alt83=1;
						}

						switch (alt83) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:954:9: S
							{
							S176=(Token)match(input,S,FOLLOW_S_in_combinator1717);  
							stream_S.add(S176);

							}
							break;

						default :
							break loop83;
						}
					}

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 954:12: -> ADJACENT
					{
						adaptor.addChild(root_0, (Object)adaptor.create(ADJACENT, "ADJACENT"));
					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:955:4: TILDE ( S )*
					{
					TILDE177=(Token)match(input,TILDE,FOLLOW_TILDE_in_combinator1727);  
					stream_TILDE.add(TILDE177);

					// cz/vutbr/web/csskit/antlr/CSS.g:955:10: ( S )*
					loop84:
					while (true) {
						int alt84=2;
						int LA84_0 = input.LA(1);
						if ( (LA84_0==S) ) {
							alt84=1;
						}

						switch (alt84) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:955:10: S
							{
							S178=(Token)match(input,S,FOLLOW_S_in_combinator1729);  
							stream_S.add(S178);

							}
							break;

						default :
							break loop84;
						}
					}

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 955:13: -> PRECEDING
					{
						adaptor.addChild(root_0, (Object)adaptor.create(PRECEDING, "PRECEDING"));
					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:956:4: S
					{
					S179=(Token)match(input,S,FOLLOW_S_in_combinator1739);  
					stream_S.add(S179);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 956:6: -> DESCENDANT
					{
						adaptor.addChild(root_0, (Object)adaptor.create(DESCENDANT, "DESCENDANT"));
					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "combinator"


	public static class selector_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "selector"
	// cz/vutbr/web/csskit/antlr/CSS.g:959:1: selector : ( ( IDENT | ASTERISK ) ( selpart )* ( S )* -> ^( SELECTOR ^( ELEMENT ( IDENT )? ) ( selpart )* ) | ( selpart )+ ( S )* -> ^( SELECTOR ( selpart )+ ) );
	public final CSSParser.selector_return selector() throws RecognitionException {
		CSSParser.selector_return retval = new CSSParser.selector_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token IDENT180=null;
		Token ASTERISK181=null;
		Token S183=null;
		Token S185=null;
		ParserRuleReturnScope selpart182 =null;
		ParserRuleReturnScope selpart184 =null;

		Object IDENT180_tree=null;
		Object ASTERISK181_tree=null;
		Object S183_tree=null;
		Object S185_tree=null;
		RewriteRuleTokenStream stream_IDENT=new RewriteRuleTokenStream(adaptor,"token IDENT");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_ASTERISK=new RewriteRuleTokenStream(adaptor,"token ASTERISK");
		RewriteRuleSubtreeStream stream_selpart=new RewriteRuleSubtreeStream(adaptor,"rule selpart");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:960:5: ( ( IDENT | ASTERISK ) ( selpart )* ( S )* -> ^( SELECTOR ^( ELEMENT ( IDENT )? ) ( selpart )* ) | ( selpart )+ ( S )* -> ^( SELECTOR ( selpart )+ ) )
			int alt91=2;
			int LA91_0 = input.LA(1);
			if ( (LA91_0==ASTERISK||LA91_0==IDENT) ) {
				alt91=1;
			}
			else if ( ((LA91_0 >= CLASSKEYWORD && LA91_0 <= COLON)||LA91_0==HASH||LA91_0==INVALID_SELPART||LA91_0==LBRACE) ) {
				alt91=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 91, 0, input);
				throw nvae;
			}

			switch (alt91) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:960:7: ( IDENT | ASTERISK ) ( selpart )* ( S )*
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:960:7: ( IDENT | ASTERISK )
					int alt86=2;
					int LA86_0 = input.LA(1);
					if ( (LA86_0==IDENT) ) {
						alt86=1;
					}
					else if ( (LA86_0==ASTERISK) ) {
						alt86=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 86, 0, input);
						throw nvae;
					}

					switch (alt86) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:960:8: IDENT
							{
							IDENT180=(Token)match(input,IDENT,FOLLOW_IDENT_in_selector1758);  
							stream_IDENT.add(IDENT180);

							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSS.g:960:16: ASTERISK
							{
							ASTERISK181=(Token)match(input,ASTERISK,FOLLOW_ASTERISK_in_selector1762);  
							stream_ASTERISK.add(ASTERISK181);

							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSS.g:960:27: ( selpart )*
					loop87:
					while (true) {
						int alt87=2;
						int LA87_0 = input.LA(1);
						if ( ((LA87_0 >= CLASSKEYWORD && LA87_0 <= COLON)||LA87_0==HASH||LA87_0==INVALID_SELPART||LA87_0==LBRACE) ) {
							alt87=1;
						}

						switch (alt87) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:960:27: selpart
							{
							pushFollow(FOLLOW_selpart_in_selector1766);
							selpart182=selpart();
							state._fsp--;

							stream_selpart.add(selpart182.getTree());
							}
							break;

						default :
							break loop87;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:960:36: ( S )*
					loop88:
					while (true) {
						int alt88=2;
						int LA88_0 = input.LA(1);
						if ( (LA88_0==S) ) {
							int LA88_4 = input.LA(2);
							if ( (LA88_4==COMMA||LA88_4==GREATER||LA88_4==LCURLY||LA88_4==PLUS||LA88_4==S||LA88_4==TILDE) ) {
								alt88=1;
							}

						}

						switch (alt88) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:960:36: S
							{
							S183=(Token)match(input,S,FOLLOW_S_in_selector1769);  
							stream_S.add(S183);

							}
							break;

						default :
							break loop88;
						}
					}

					// AST REWRITE
					// elements: selpart, IDENT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 961:6: -> ^( SELECTOR ^( ELEMENT ( IDENT )? ) ( selpart )* )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:961:9: ^( SELECTOR ^( ELEMENT ( IDENT )? ) ( selpart )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SELECTOR, "SELECTOR"), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:961:20: ^( ELEMENT ( IDENT )? )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(ELEMENT, "ELEMENT"), root_2);
						// cz/vutbr/web/csskit/antlr/CSS.g:961:30: ( IDENT )?
						if ( stream_IDENT.hasNext() ) {
							adaptor.addChild(root_2, stream_IDENT.nextNode());
						}
						stream_IDENT.reset();

						adaptor.addChild(root_1, root_2);
						}

						// cz/vutbr/web/csskit/antlr/CSS.g:961:38: ( selpart )*
						while ( stream_selpart.hasNext() ) {
							adaptor.addChild(root_1, stream_selpart.nextTree());
						}
						stream_selpart.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:962:7: ( selpart )+ ( S )*
					{
					// cz/vutbr/web/csskit/antlr/CSS.g:962:7: ( selpart )+
					int cnt89=0;
					loop89:
					while (true) {
						int alt89=2;
						int LA89_0 = input.LA(1);
						if ( ((LA89_0 >= CLASSKEYWORD && LA89_0 <= COLON)||LA89_0==HASH||LA89_0==INVALID_SELPART||LA89_0==LBRACE) ) {
							alt89=1;
						}

						switch (alt89) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:962:7: selpart
							{
							pushFollow(FOLLOW_selpart_in_selector1799);
							selpart184=selpart();
							state._fsp--;

							stream_selpart.add(selpart184.getTree());
							}
							break;

						default :
							if ( cnt89 >= 1 ) break loop89;
							EarlyExitException eee = new EarlyExitException(89, input);
							throw eee;
						}
						cnt89++;
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:962:16: ( S )*
					loop90:
					while (true) {
						int alt90=2;
						int LA90_0 = input.LA(1);
						if ( (LA90_0==S) ) {
							int LA90_4 = input.LA(2);
							if ( (LA90_4==COMMA||LA90_4==GREATER||LA90_4==LCURLY||LA90_4==PLUS||LA90_4==S||LA90_4==TILDE) ) {
								alt90=1;
							}

						}

						switch (alt90) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:962:16: S
							{
							S185=(Token)match(input,S,FOLLOW_S_in_selector1802);  
							stream_S.add(S185);

							}
							break;

						default :
							break loop90;
						}
					}

					// AST REWRITE
					// elements: selpart
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 963:9: -> ^( SELECTOR ( selpart )+ )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:963:12: ^( SELECTOR ( selpart )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SELECTOR, "SELECTOR"), root_1);
						if ( !(stream_selpart.hasNext()) ) {
							throw new RewriteEarlyExitException();
						}
						while ( stream_selpart.hasNext() ) {
							adaptor.addChild(root_1, stream_selpart.nextTree());
						}
						stream_selpart.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			      retval.tree = invalidFallback(CSSLexer.INVALID_SELECTOR, "INVALID_SELECTOR", re);
				  
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "selector"


	public static class selpart_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "selpart"
	// cz/vutbr/web/csskit/antlr/CSS.g:969:1: selpart : ( HASH | CLASSKEYWORD | LBRACE ( S )* attribute RBRACE -> ^( ATTRIBUTE attribute ) | pseudo | INVALID_SELPART );
	public final CSSParser.selpart_return selpart() throws RecognitionException {
		CSSParser.selpart_return retval = new CSSParser.selpart_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token HASH186=null;
		Token CLASSKEYWORD187=null;
		Token LBRACE188=null;
		Token S189=null;
		Token RBRACE191=null;
		Token INVALID_SELPART193=null;
		ParserRuleReturnScope attribute190 =null;
		ParserRuleReturnScope pseudo192 =null;

		Object HASH186_tree=null;
		Object CLASSKEYWORD187_tree=null;
		Object LBRACE188_tree=null;
		Object S189_tree=null;
		Object RBRACE191_tree=null;
		Object INVALID_SELPART193_tree=null;
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
		RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
		RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:970:5: ( HASH | CLASSKEYWORD | LBRACE ( S )* attribute RBRACE -> ^( ATTRIBUTE attribute ) | pseudo | INVALID_SELPART )
			int alt93=5;
			switch ( input.LA(1) ) {
			case HASH:
				{
				alt93=1;
				}
				break;
			case CLASSKEYWORD:
				{
				alt93=2;
				}
				break;
			case LBRACE:
				{
				alt93=3;
				}
				break;
			case COLON:
				{
				alt93=4;
				}
				break;
			case INVALID_SELPART:
				{
				alt93=5;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 93, 0, input);
				throw nvae;
			}
			switch (alt93) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:970:8: HASH
					{
					root_0 = (Object)adaptor.nil();


					HASH186=(Token)match(input,HASH,FOLLOW_HASH_in_selpart1849); 
					HASH186_tree = (Object)adaptor.create(HASH186);
					adaptor.addChild(root_0, HASH186_tree);

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:971:7: CLASSKEYWORD
					{
					root_0 = (Object)adaptor.nil();


					CLASSKEYWORD187=(Token)match(input,CLASSKEYWORD,FOLLOW_CLASSKEYWORD_in_selpart1857); 
					CLASSKEYWORD187_tree = (Object)adaptor.create(CLASSKEYWORD187);
					adaptor.addChild(root_0, CLASSKEYWORD187_tree);

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:972:6: LBRACE ( S )* attribute RBRACE
					{
					LBRACE188=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_selpart1864);  
					stream_LBRACE.add(LBRACE188);

					// cz/vutbr/web/csskit/antlr/CSS.g:972:13: ( S )*
					loop92:
					while (true) {
						int alt92=2;
						int LA92_0 = input.LA(1);
						if ( (LA92_0==S) ) {
							alt92=1;
						}

						switch (alt92) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:972:13: S
							{
							S189=(Token)match(input,S,FOLLOW_S_in_selpart1866);  
							stream_S.add(S189);

							}
							break;

						default :
							break loop92;
						}
					}

					pushFollow(FOLLOW_attribute_in_selpart1869);
					attribute190=attribute();
					state._fsp--;

					stream_attribute.add(attribute190.getTree());
					RBRACE191=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_selpart1871);  
					stream_RBRACE.add(RBRACE191);

					// AST REWRITE
					// elements: attribute
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 972:33: -> ^( ATTRIBUTE attribute )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:972:36: ^( ATTRIBUTE attribute )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ATTRIBUTE, "ATTRIBUTE"), root_1);
						adaptor.addChild(root_1, stream_attribute.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:973:7: pseudo
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_pseudo_in_selpart1887);
					pseudo192=pseudo();
					state._fsp--;

					adaptor.addChild(root_0, pseudo192.getTree());

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:974:7: INVALID_SELPART
					{
					root_0 = (Object)adaptor.nil();


					INVALID_SELPART193=(Token)match(input,INVALID_SELPART,FOLLOW_INVALID_SELPART_in_selpart1895); 
					INVALID_SELPART193_tree = (Object)adaptor.create(INVALID_SELPART193);
					adaptor.addChild(root_0, INVALID_SELPART193_tree);

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			      retval.tree = invalidFallback(CSSLexer.INVALID_SELPART, "INVALID_SELPART", re);
				  
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "selpart"


	public static class attribute_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "attribute"
	// cz/vutbr/web/csskit/antlr/CSS.g:980:1: attribute : IDENT ( S !)* ( ( EQUALS | INCLUDES | DASHMATCH | STARTSWITH | ENDSWITH | CONTAINS ) ( S !)* ( IDENT | string ) ( S !)* )? ;
	public final CSSParser.attribute_return attribute() throws RecognitionException {
		CSSParser.attribute_return retval = new CSSParser.attribute_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token IDENT194=null;
		Token S195=null;
		Token set196=null;
		Token S197=null;
		Token IDENT198=null;
		Token S200=null;
		ParserRuleReturnScope string199 =null;

		Object IDENT194_tree=null;
		Object S195_tree=null;
		Object set196_tree=null;
		Object S197_tree=null;
		Object IDENT198_tree=null;
		Object S200_tree=null;

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:981:2: ( IDENT ( S !)* ( ( EQUALS | INCLUDES | DASHMATCH | STARTSWITH | ENDSWITH | CONTAINS ) ( S !)* ( IDENT | string ) ( S !)* )? )
			// cz/vutbr/web/csskit/antlr/CSS.g:981:4: IDENT ( S !)* ( ( EQUALS | INCLUDES | DASHMATCH | STARTSWITH | ENDSWITH | CONTAINS ) ( S !)* ( IDENT | string ) ( S !)* )?
			{
			root_0 = (Object)adaptor.nil();


			IDENT194=(Token)match(input,IDENT,FOLLOW_IDENT_in_attribute1919); 
			IDENT194_tree = (Object)adaptor.create(IDENT194);
			adaptor.addChild(root_0, IDENT194_tree);

			// cz/vutbr/web/csskit/antlr/CSS.g:981:11: ( S !)*
			loop94:
			while (true) {
				int alt94=2;
				int LA94_0 = input.LA(1);
				if ( (LA94_0==S) ) {
					alt94=1;
				}

				switch (alt94) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:981:11: S !
					{
					S195=(Token)match(input,S,FOLLOW_S_in_attribute1921); 
					}
					break;

				default :
					break loop94;
				}
			}

			// cz/vutbr/web/csskit/antlr/CSS.g:982:4: ( ( EQUALS | INCLUDES | DASHMATCH | STARTSWITH | ENDSWITH | CONTAINS ) ( S !)* ( IDENT | string ) ( S !)* )?
			int alt98=2;
			int LA98_0 = input.LA(1);
			if ( (LA98_0==CONTAINS||LA98_0==DASHMATCH||(LA98_0 >= ENDSWITH && LA98_0 <= EQUALS)||LA98_0==INCLUDES||LA98_0==STARTSWITH) ) {
				alt98=1;
			}
			switch (alt98) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:982:5: ( EQUALS | INCLUDES | DASHMATCH | STARTSWITH | ENDSWITH | CONTAINS ) ( S !)* ( IDENT | string ) ( S !)*
					{
					set196=input.LT(1);
					if ( input.LA(1)==CONTAINS||input.LA(1)==DASHMATCH||(input.LA(1) >= ENDSWITH && input.LA(1) <= EQUALS)||input.LA(1)==INCLUDES||input.LA(1)==STARTSWITH ) {
						input.consume();
						adaptor.addChild(root_0, (Object)adaptor.create(set196));
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// cz/vutbr/web/csskit/antlr/CSS.g:982:73: ( S !)*
					loop95:
					while (true) {
						int alt95=2;
						int LA95_0 = input.LA(1);
						if ( (LA95_0==S) ) {
							alt95=1;
						}

						switch (alt95) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:982:73: S !
							{
							S197=(Token)match(input,S,FOLLOW_S_in_attribute1953); 
							}
							break;

						default :
							break loop95;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:982:76: ( IDENT | string )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==IDENT) ) {
						alt96=1;
					}
					else if ( (LA96_0==INVALID_STRING||LA96_0==STRING) ) {
						alt96=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 96, 0, input);
						throw nvae;
					}

					switch (alt96) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:982:77: IDENT
							{
							IDENT198=(Token)match(input,IDENT,FOLLOW_IDENT_in_attribute1958); 
							IDENT198_tree = (Object)adaptor.create(IDENT198);
							adaptor.addChild(root_0, IDENT198_tree);

							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSS.g:982:85: string
							{
							pushFollow(FOLLOW_string_in_attribute1962);
							string199=string();
							state._fsp--;

							adaptor.addChild(root_0, string199.getTree());

							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSS.g:982:94: ( S !)*
					loop97:
					while (true) {
						int alt97=2;
						int LA97_0 = input.LA(1);
						if ( (LA97_0==S) ) {
							alt97=1;
						}

						switch (alt97) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:982:94: S !
							{
							S200=(Token)match(input,S,FOLLOW_S_in_attribute1965); 
							}
							break;

						default :
							break loop97;
						}
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "attribute"


	public static class pseudo_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "pseudo"
	// cz/vutbr/web/csskit/antlr/CSS.g:985:1: pseudo : pseudocolon ^ ( IDENT | FUNCTION ( S !)* ( IDENT | ( MINUS )? NUMBER | ( MINUS )? INDEX ) ( S !)* RPAREN !) ;
	public final CSSParser.pseudo_return pseudo() throws RecognitionException {
		CSSParser.pseudo_return retval = new CSSParser.pseudo_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token IDENT202=null;
		Token FUNCTION203=null;
		Token S204=null;
		Token IDENT205=null;
		Token MINUS206=null;
		Token NUMBER207=null;
		Token MINUS208=null;
		Token INDEX209=null;
		Token S210=null;
		Token RPAREN211=null;
		ParserRuleReturnScope pseudocolon201 =null;

		Object IDENT202_tree=null;
		Object FUNCTION203_tree=null;
		Object S204_tree=null;
		Object IDENT205_tree=null;
		Object MINUS206_tree=null;
		Object NUMBER207_tree=null;
		Object MINUS208_tree=null;
		Object INDEX209_tree=null;
		Object S210_tree=null;
		Object RPAREN211_tree=null;

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:986:2: ( pseudocolon ^ ( IDENT | FUNCTION ( S !)* ( IDENT | ( MINUS )? NUMBER | ( MINUS )? INDEX ) ( S !)* RPAREN !) )
			// cz/vutbr/web/csskit/antlr/CSS.g:986:4: pseudocolon ^ ( IDENT | FUNCTION ( S !)* ( IDENT | ( MINUS )? NUMBER | ( MINUS )? INDEX ) ( S !)* RPAREN !)
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_pseudocolon_in_pseudo1980);
			pseudocolon201=pseudocolon();
			state._fsp--;

			root_0 = (Object)adaptor.becomeRoot(pseudocolon201.getTree(), root_0);
			// cz/vutbr/web/csskit/antlr/CSS.g:986:17: ( IDENT | FUNCTION ( S !)* ( IDENT | ( MINUS )? NUMBER | ( MINUS )? INDEX ) ( S !)* RPAREN !)
			int alt104=2;
			int LA104_0 = input.LA(1);
			if ( (LA104_0==IDENT) ) {
				alt104=1;
			}
			else if ( (LA104_0==FUNCTION) ) {
				alt104=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 104, 0, input);
				throw nvae;
			}

			switch (alt104) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:986:18: IDENT
					{
					IDENT202=(Token)match(input,IDENT,FOLLOW_IDENT_in_pseudo1984); 
					IDENT202_tree = (Object)adaptor.create(IDENT202);
					adaptor.addChild(root_0, IDENT202_tree);

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:986:26: FUNCTION ( S !)* ( IDENT | ( MINUS )? NUMBER | ( MINUS )? INDEX ) ( S !)* RPAREN !
					{
					FUNCTION203=(Token)match(input,FUNCTION,FOLLOW_FUNCTION_in_pseudo1988); 
					FUNCTION203_tree = (Object)adaptor.create(FUNCTION203);
					adaptor.addChild(root_0, FUNCTION203_tree);

					// cz/vutbr/web/csskit/antlr/CSS.g:986:36: ( S !)*
					loop99:
					while (true) {
						int alt99=2;
						int LA99_0 = input.LA(1);
						if ( (LA99_0==S) ) {
							alt99=1;
						}

						switch (alt99) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:986:36: S !
							{
							S204=(Token)match(input,S,FOLLOW_S_in_pseudo1990); 
							}
							break;

						default :
							break loop99;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:986:39: ( IDENT | ( MINUS )? NUMBER | ( MINUS )? INDEX )
					int alt102=3;
					switch ( input.LA(1) ) {
					case IDENT:
						{
						alt102=1;
						}
						break;
					case MINUS:
						{
						int LA102_2 = input.LA(2);
						if ( (LA102_2==NUMBER) ) {
							alt102=2;
						}
						else if ( (LA102_2==INDEX) ) {
							alt102=3;
						}

						else {
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 102, 2, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case NUMBER:
						{
						alt102=2;
						}
						break;
					case INDEX:
						{
						alt102=3;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 102, 0, input);
						throw nvae;
					}
					switch (alt102) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:986:40: IDENT
							{
							IDENT205=(Token)match(input,IDENT,FOLLOW_IDENT_in_pseudo1995); 
							IDENT205_tree = (Object)adaptor.create(IDENT205);
							adaptor.addChild(root_0, IDENT205_tree);

							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSS.g:986:48: ( MINUS )? NUMBER
							{
							// cz/vutbr/web/csskit/antlr/CSS.g:986:48: ( MINUS )?
							int alt100=2;
							int LA100_0 = input.LA(1);
							if ( (LA100_0==MINUS) ) {
								alt100=1;
							}
							switch (alt100) {
								case 1 :
									// cz/vutbr/web/csskit/antlr/CSS.g:986:48: MINUS
									{
									MINUS206=(Token)match(input,MINUS,FOLLOW_MINUS_in_pseudo1999); 
									MINUS206_tree = (Object)adaptor.create(MINUS206);
									adaptor.addChild(root_0, MINUS206_tree);

									}
									break;

							}

							NUMBER207=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_pseudo2002); 
							NUMBER207_tree = (Object)adaptor.create(NUMBER207);
							adaptor.addChild(root_0, NUMBER207_tree);

							}
							break;
						case 3 :
							// cz/vutbr/web/csskit/antlr/CSS.g:986:64: ( MINUS )? INDEX
							{
							// cz/vutbr/web/csskit/antlr/CSS.g:986:64: ( MINUS )?
							int alt101=2;
							int LA101_0 = input.LA(1);
							if ( (LA101_0==MINUS) ) {
								alt101=1;
							}
							switch (alt101) {
								case 1 :
									// cz/vutbr/web/csskit/antlr/CSS.g:986:64: MINUS
									{
									MINUS208=(Token)match(input,MINUS,FOLLOW_MINUS_in_pseudo2006); 
									MINUS208_tree = (Object)adaptor.create(MINUS208);
									adaptor.addChild(root_0, MINUS208_tree);

									}
									break;

							}

							INDEX209=(Token)match(input,INDEX,FOLLOW_INDEX_in_pseudo2009); 
							INDEX209_tree = (Object)adaptor.create(INDEX209);
							adaptor.addChild(root_0, INDEX209_tree);

							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSS.g:986:79: ( S !)*
					loop103:
					while (true) {
						int alt103=2;
						int LA103_0 = input.LA(1);
						if ( (LA103_0==S) ) {
							alt103=1;
						}

						switch (alt103) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:986:79: S !
							{
							S210=(Token)match(input,S,FOLLOW_S_in_pseudo2012); 
							}
							break;

						default :
							break loop103;
						}
					}

					RPAREN211=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_pseudo2016); 
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {

			     retval.tree = invalidFallback(CSSLexer.INVALID_SELPART, "INVALID_SELPART", re);
			  
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "pseudo"


	public static class pseudocolon_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "pseudocolon"
	// cz/vutbr/web/csskit/antlr/CSS.g:992:1: pseudocolon : COLON ( COLON )? -> PSEUDO ;
	public final CSSParser.pseudocolon_return pseudocolon() throws RecognitionException {
		CSSParser.pseudocolon_return retval = new CSSParser.pseudocolon_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COLON212=null;
		Token COLON213=null;

		Object COLON212_tree=null;
		Object COLON213_tree=null;
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:993:2: ( COLON ( COLON )? -> PSEUDO )
			// cz/vutbr/web/csskit/antlr/CSS.g:993:4: COLON ( COLON )?
			{
			COLON212=(Token)match(input,COLON,FOLLOW_COLON_in_pseudocolon2037);  
			stream_COLON.add(COLON212);

			// cz/vutbr/web/csskit/antlr/CSS.g:993:10: ( COLON )?
			int alt105=2;
			int LA105_0 = input.LA(1);
			if ( (LA105_0==COLON) ) {
				alt105=1;
			}
			switch (alt105) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:993:10: COLON
					{
					COLON213=(Token)match(input,COLON,FOLLOW_COLON_in_pseudocolon2039);  
					stream_COLON.add(COLON213);

					}
					break;

			}

			// AST REWRITE
			// elements: 
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 993:17: -> PSEUDO
			{
				adaptor.addChild(root_0, (Object)adaptor.create(PSEUDO, "PSEUDO"));
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "pseudocolon"


	public static class string_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "string"
	// cz/vutbr/web/csskit/antlr/CSS.g:996:1: string : ( STRING | INVALID_STRING );
	public final CSSParser.string_return string() throws RecognitionException {
		CSSParser.string_return retval = new CSSParser.string_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set214=null;

		Object set214_tree=null;

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:997:2: ( STRING | INVALID_STRING )
			// cz/vutbr/web/csskit/antlr/CSS.g:
			{
			root_0 = (Object)adaptor.nil();


			set214=input.LT(1);
			if ( input.LA(1)==INVALID_STRING||input.LA(1)==STRING ) {
				input.consume();
				adaptor.addChild(root_0, (Object)adaptor.create(set214));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "string"


	public static class any_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "any"
	// cz/vutbr/web/csskit/antlr/CSS.g:1002:1: any : ( IDENT -> IDENT | CLASSKEYWORD -> CLASSKEYWORD | NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | HASH -> HASH | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COLON -> COLON | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | ASTERISK -> ASTERISK | FUNCTION ( S )* ( any )* RPAREN -> ^( FUNCTION ( any )* ) | DASHMATCH -> DASHMATCH | LPAREN ( any )* RPAREN -> ^( PARENBLOCK ( any )* ) | LBRACE ( any )* RBRACE -> ^( BRACEBLOCK ( any )* ) ) ! ( S )* ;
	public final CSSParser.any_return any() throws RecognitionException {
		CSSParser.any_return retval = new CSSParser.any_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token IDENT215=null;
		Token CLASSKEYWORD216=null;
		Token NUMBER217=null;
		Token PERCENTAGE218=null;
		Token DIMENSION219=null;
		Token URI221=null;
		Token HASH222=null;
		Token UNIRANGE223=null;
		Token INCLUDES224=null;
		Token COLON225=null;
		Token COMMA226=null;
		Token GREATER227=null;
		Token LESS228=null;
		Token QUESTION229=null;
		Token PERCENT230=null;
		Token EQUALS231=null;
		Token SLASH232=null;
		Token EXCLAMATION233=null;
		Token MINUS234=null;
		Token PLUS235=null;
		Token ASTERISK236=null;
		Token FUNCTION237=null;
		Token S238=null;
		Token RPAREN240=null;
		Token DASHMATCH241=null;
		Token LPAREN242=null;
		Token RPAREN244=null;
		Token LBRACE245=null;
		Token RBRACE247=null;
		Token S248=null;
		ParserRuleReturnScope string220 =null;
		ParserRuleReturnScope any239 =null;
		ParserRuleReturnScope any243 =null;
		ParserRuleReturnScope any246 =null;

		Object IDENT215_tree=null;
		Object CLASSKEYWORD216_tree=null;
		Object NUMBER217_tree=null;
		Object PERCENTAGE218_tree=null;
		Object DIMENSION219_tree=null;
		Object URI221_tree=null;
		Object HASH222_tree=null;
		Object UNIRANGE223_tree=null;
		Object INCLUDES224_tree=null;
		Object COLON225_tree=null;
		Object COMMA226_tree=null;
		Object GREATER227_tree=null;
		Object LESS228_tree=null;
		Object QUESTION229_tree=null;
		Object PERCENT230_tree=null;
		Object EQUALS231_tree=null;
		Object SLASH232_tree=null;
		Object EXCLAMATION233_tree=null;
		Object MINUS234_tree=null;
		Object PLUS235_tree=null;
		Object ASTERISK236_tree=null;
		Object FUNCTION237_tree=null;
		Object S238_tree=null;
		Object RPAREN240_tree=null;
		Object DASHMATCH241_tree=null;
		Object LPAREN242_tree=null;
		Object RPAREN244_tree=null;
		Object LBRACE245_tree=null;
		Object RBRACE247_tree=null;
		Object S248_tree=null;
		RewriteRuleTokenStream stream_FUNCTION=new RewriteRuleTokenStream(adaptor,"token FUNCTION");
		RewriteRuleTokenStream stream_PERCENT=new RewriteRuleTokenStream(adaptor,"token PERCENT");
		RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
		RewriteRuleTokenStream stream_CLASSKEYWORD=new RewriteRuleTokenStream(adaptor,"token CLASSKEYWORD");
		RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_HASH=new RewriteRuleTokenStream(adaptor,"token HASH");
		RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");
		RewriteRuleTokenStream stream_PERCENTAGE=new RewriteRuleTokenStream(adaptor,"token PERCENTAGE");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_ASTERISK=new RewriteRuleTokenStream(adaptor,"token ASTERISK");
		RewriteRuleTokenStream stream_URI=new RewriteRuleTokenStream(adaptor,"token URI");
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_INCLUDES=new RewriteRuleTokenStream(adaptor,"token INCLUDES");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_GREATER=new RewriteRuleTokenStream(adaptor,"token GREATER");
		RewriteRuleTokenStream stream_SLASH=new RewriteRuleTokenStream(adaptor,"token SLASH");
		RewriteRuleTokenStream stream_DASHMATCH=new RewriteRuleTokenStream(adaptor,"token DASHMATCH");
		RewriteRuleTokenStream stream_QUESTION=new RewriteRuleTokenStream(adaptor,"token QUESTION");
		RewriteRuleTokenStream stream_EXCLAMATION=new RewriteRuleTokenStream(adaptor,"token EXCLAMATION");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_LESS=new RewriteRuleTokenStream(adaptor,"token LESS");
		RewriteRuleTokenStream stream_IDENT=new RewriteRuleTokenStream(adaptor,"token IDENT");
		RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
		RewriteRuleTokenStream stream_DIMENSION=new RewriteRuleTokenStream(adaptor,"token DIMENSION");
		RewriteRuleTokenStream stream_UNIRANGE=new RewriteRuleTokenStream(adaptor,"token UNIRANGE");
		RewriteRuleSubtreeStream stream_string=new RewriteRuleSubtreeStream(adaptor,"rule string");
		RewriteRuleSubtreeStream stream_any=new RewriteRuleSubtreeStream(adaptor,"rule any");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1003:2: ( ( IDENT -> IDENT | CLASSKEYWORD -> CLASSKEYWORD | NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | HASH -> HASH | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COLON -> COLON | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | ASTERISK -> ASTERISK | FUNCTION ( S )* ( any )* RPAREN -> ^( FUNCTION ( any )* ) | DASHMATCH -> DASHMATCH | LPAREN ( any )* RPAREN -> ^( PARENBLOCK ( any )* ) | LBRACE ( any )* RBRACE -> ^( BRACEBLOCK ( any )* ) ) ! ( S )* )
			// cz/vutbr/web/csskit/antlr/CSS.g:1003:4: ( IDENT -> IDENT | CLASSKEYWORD -> CLASSKEYWORD | NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | HASH -> HASH | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COLON -> COLON | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | ASTERISK -> ASTERISK | FUNCTION ( S )* ( any )* RPAREN -> ^( FUNCTION ( any )* ) | DASHMATCH -> DASHMATCH | LPAREN ( any )* RPAREN -> ^( PARENBLOCK ( any )* ) | LBRACE ( any )* RBRACE -> ^( BRACEBLOCK ( any )* ) ) ! ( S )*
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1003:4: ( IDENT -> IDENT | CLASSKEYWORD -> CLASSKEYWORD | NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | HASH -> HASH | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COLON -> COLON | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | ASTERISK -> ASTERISK | FUNCTION ( S )* ( any )* RPAREN -> ^( FUNCTION ( any )* ) | DASHMATCH -> DASHMATCH | LPAREN ( any )* RPAREN -> ^( PARENBLOCK ( any )* ) | LBRACE ( any )* RBRACE -> ^( BRACEBLOCK ( any )* ) )
			int alt110=26;
			switch ( input.LA(1) ) {
			case IDENT:
				{
				alt110=1;
				}
				break;
			case CLASSKEYWORD:
				{
				alt110=2;
				}
				break;
			case NUMBER:
				{
				alt110=3;
				}
				break;
			case PERCENTAGE:
				{
				alt110=4;
				}
				break;
			case DIMENSION:
				{
				alt110=5;
				}
				break;
			case INVALID_STRING:
			case STRING:
				{
				alt110=6;
				}
				break;
			case URI:
				{
				alt110=7;
				}
				break;
			case HASH:
				{
				alt110=8;
				}
				break;
			case UNIRANGE:
				{
				alt110=9;
				}
				break;
			case INCLUDES:
				{
				alt110=10;
				}
				break;
			case COLON:
				{
				alt110=11;
				}
				break;
			case COMMA:
				{
				alt110=12;
				}
				break;
			case GREATER:
				{
				alt110=13;
				}
				break;
			case LESS:
				{
				alt110=14;
				}
				break;
			case QUESTION:
				{
				alt110=15;
				}
				break;
			case PERCENT:
				{
				alt110=16;
				}
				break;
			case EQUALS:
				{
				alt110=17;
				}
				break;
			case SLASH:
				{
				alt110=18;
				}
				break;
			case EXCLAMATION:
				{
				alt110=19;
				}
				break;
			case MINUS:
				{
				alt110=20;
				}
				break;
			case PLUS:
				{
				alt110=21;
				}
				break;
			case ASTERISK:
				{
				alt110=22;
				}
				break;
			case FUNCTION:
				{
				alt110=23;
				}
				break;
			case DASHMATCH:
				{
				alt110=24;
				}
				break;
			case LPAREN:
				{
				alt110=25;
				}
				break;
			case LBRACE:
				{
				alt110=26;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 110, 0, input);
				throw nvae;
			}
			switch (alt110) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1003:6: IDENT
					{
					IDENT215=(Token)match(input,IDENT,FOLLOW_IDENT_in_any2076);  
					stream_IDENT.add(IDENT215);

					// AST REWRITE
					// elements: IDENT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1003:12: -> IDENT
					{
						adaptor.addChild(root_0, stream_IDENT.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1004:6: CLASSKEYWORD
					{
					CLASSKEYWORD216=(Token)match(input,CLASSKEYWORD,FOLLOW_CLASSKEYWORD_in_any2087);  
					stream_CLASSKEYWORD.add(CLASSKEYWORD216);

					// AST REWRITE
					// elements: CLASSKEYWORD
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1004:19: -> CLASSKEYWORD
					{
						adaptor.addChild(root_0, stream_CLASSKEYWORD.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1005:6: NUMBER
					{
					NUMBER217=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_any2098);  
					stream_NUMBER.add(NUMBER217);

					// AST REWRITE
					// elements: NUMBER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1005:13: -> NUMBER
					{
						adaptor.addChild(root_0, stream_NUMBER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1006:6: PERCENTAGE
					{
					PERCENTAGE218=(Token)match(input,PERCENTAGE,FOLLOW_PERCENTAGE_in_any2109);  
					stream_PERCENTAGE.add(PERCENTAGE218);

					// AST REWRITE
					// elements: PERCENTAGE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1006:17: -> PERCENTAGE
					{
						adaptor.addChild(root_0, stream_PERCENTAGE.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1007:6: DIMENSION
					{
					DIMENSION219=(Token)match(input,DIMENSION,FOLLOW_DIMENSION_in_any2119);  
					stream_DIMENSION.add(DIMENSION219);

					// AST REWRITE
					// elements: DIMENSION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1007:16: -> DIMENSION
					{
						adaptor.addChild(root_0, stream_DIMENSION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1008:6: string
					{
					pushFollow(FOLLOW_string_in_any2130);
					string220=string();
					state._fsp--;

					stream_string.add(string220.getTree());
					// AST REWRITE
					// elements: string
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1008:13: -> string
					{
						adaptor.addChild(root_0, stream_string.nextTree());
					}


					retval.tree = root_0;

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1009:9: URI
					{
					URI221=(Token)match(input,URI,FOLLOW_URI_in_any2144);  
					stream_URI.add(URI221);

					// AST REWRITE
					// elements: URI
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1009:16: -> URI
					{
						adaptor.addChild(root_0, stream_URI.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 8 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1010:9: HASH
					{
					HASH222=(Token)match(input,HASH,FOLLOW_HASH_in_any2161);  
					stream_HASH.add(HASH222);

					// AST REWRITE
					// elements: HASH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1010:14: -> HASH
					{
						adaptor.addChild(root_0, stream_HASH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 9 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1011:9: UNIRANGE
					{
					UNIRANGE223=(Token)match(input,UNIRANGE,FOLLOW_UNIRANGE_in_any2175);  
					stream_UNIRANGE.add(UNIRANGE223);

					// AST REWRITE
					// elements: UNIRANGE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1011:18: -> UNIRANGE
					{
						adaptor.addChild(root_0, stream_UNIRANGE.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 10 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1012:9: INCLUDES
					{
					INCLUDES224=(Token)match(input,INCLUDES,FOLLOW_INCLUDES_in_any2189);  
					stream_INCLUDES.add(INCLUDES224);

					// AST REWRITE
					// elements: INCLUDES
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1012:18: -> INCLUDES
					{
						adaptor.addChild(root_0, stream_INCLUDES.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 11 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1013:9: COLON
					{
					COLON225=(Token)match(input,COLON,FOLLOW_COLON_in_any2203);  
					stream_COLON.add(COLON225);

					// AST REWRITE
					// elements: COLON
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1013:15: -> COLON
					{
						adaptor.addChild(root_0, stream_COLON.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 12 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1014:9: COMMA
					{
					COMMA226=(Token)match(input,COMMA,FOLLOW_COMMA_in_any2217);  
					stream_COMMA.add(COMMA226);

					// AST REWRITE
					// elements: COMMA
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1014:15: -> COMMA
					{
						adaptor.addChild(root_0, stream_COMMA.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 13 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1015:9: GREATER
					{
					GREATER227=(Token)match(input,GREATER,FOLLOW_GREATER_in_any2231);  
					stream_GREATER.add(GREATER227);

					// AST REWRITE
					// elements: GREATER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1015:17: -> GREATER
					{
						adaptor.addChild(root_0, stream_GREATER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 14 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1016:9: LESS
					{
					LESS228=(Token)match(input,LESS,FOLLOW_LESS_in_any2245);  
					stream_LESS.add(LESS228);

					// AST REWRITE
					// elements: LESS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1016:14: -> LESS
					{
						adaptor.addChild(root_0, stream_LESS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 15 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1017:9: QUESTION
					{
					QUESTION229=(Token)match(input,QUESTION,FOLLOW_QUESTION_in_any2259);  
					stream_QUESTION.add(QUESTION229);

					// AST REWRITE
					// elements: QUESTION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1017:18: -> QUESTION
					{
						adaptor.addChild(root_0, stream_QUESTION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 16 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1018:9: PERCENT
					{
					PERCENT230=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_any2273);  
					stream_PERCENT.add(PERCENT230);

					// AST REWRITE
					// elements: PERCENT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1018:17: -> PERCENT
					{
						adaptor.addChild(root_0, stream_PERCENT.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 17 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1019:9: EQUALS
					{
					EQUALS231=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_any2287);  
					stream_EQUALS.add(EQUALS231);

					// AST REWRITE
					// elements: EQUALS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1019:16: -> EQUALS
					{
						adaptor.addChild(root_0, stream_EQUALS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 18 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1020:9: SLASH
					{
					SLASH232=(Token)match(input,SLASH,FOLLOW_SLASH_in_any2301);  
					stream_SLASH.add(SLASH232);

					// AST REWRITE
					// elements: SLASH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1020:15: -> SLASH
					{
						adaptor.addChild(root_0, stream_SLASH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 19 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1021:9: EXCLAMATION
					{
					EXCLAMATION233=(Token)match(input,EXCLAMATION,FOLLOW_EXCLAMATION_in_any2315);  
					stream_EXCLAMATION.add(EXCLAMATION233);

					// AST REWRITE
					// elements: EXCLAMATION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1021:21: -> EXCLAMATION
					{
						adaptor.addChild(root_0, stream_EXCLAMATION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 20 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1022:6: MINUS
					{
					MINUS234=(Token)match(input,MINUS,FOLLOW_MINUS_in_any2326);  
					stream_MINUS.add(MINUS234);

					// AST REWRITE
					// elements: MINUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1022:12: -> MINUS
					{
						adaptor.addChild(root_0, stream_MINUS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 21 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1023:6: PLUS
					{
					PLUS235=(Token)match(input,PLUS,FOLLOW_PLUS_in_any2337);  
					stream_PLUS.add(PLUS235);

					// AST REWRITE
					// elements: PLUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1023:11: -> PLUS
					{
						adaptor.addChild(root_0, stream_PLUS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 22 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1024:6: ASTERISK
					{
					ASTERISK236=(Token)match(input,ASTERISK,FOLLOW_ASTERISK_in_any2348);  
					stream_ASTERISK.add(ASTERISK236);

					// AST REWRITE
					// elements: ASTERISK
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1024:15: -> ASTERISK
					{
						adaptor.addChild(root_0, stream_ASTERISK.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 23 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1025:9: FUNCTION ( S )* ( any )* RPAREN
					{
					FUNCTION237=(Token)match(input,FUNCTION,FOLLOW_FUNCTION_in_any2365);  
					stream_FUNCTION.add(FUNCTION237);

					// cz/vutbr/web/csskit/antlr/CSS.g:1025:18: ( S )*
					loop106:
					while (true) {
						int alt106=2;
						int LA106_0 = input.LA(1);
						if ( (LA106_0==S) ) {
							alt106=1;
						}

						switch (alt106) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1025:18: S
							{
							S238=(Token)match(input,S,FOLLOW_S_in_any2367);  
							stream_S.add(S238);

							}
							break;

						default :
							break loop106;
						}
					}

					// cz/vutbr/web/csskit/antlr/CSS.g:1025:21: ( any )*
					loop107:
					while (true) {
						int alt107=2;
						int LA107_0 = input.LA(1);
						if ( (LA107_0==ASTERISK||(LA107_0 >= CLASSKEYWORD && LA107_0 <= COMMA)||LA107_0==DASHMATCH||LA107_0==DIMENSION||LA107_0==EQUALS||LA107_0==EXCLAMATION||(LA107_0 >= FUNCTION && LA107_0 <= IDENT)||LA107_0==INCLUDES||LA107_0==INVALID_STRING||LA107_0==LBRACE||(LA107_0 >= LESS && LA107_0 <= LPAREN)||LA107_0==MINUS||LA107_0==NUMBER||(LA107_0 >= PERCENT && LA107_0 <= PLUS)||LA107_0==QUESTION||LA107_0==SLASH||LA107_0==STRING||(LA107_0 >= UNIRANGE && LA107_0 <= URI)) ) {
							alt107=1;
						}

						switch (alt107) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1025:21: any
							{
							pushFollow(FOLLOW_any_in_any2370);
							any239=any();
							state._fsp--;

							stream_any.add(any239.getTree());
							}
							break;

						default :
							break loop107;
						}
					}

					RPAREN240=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_any2373);  
					stream_RPAREN.add(RPAREN240);

					// AST REWRITE
					// elements: any, FUNCTION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1025:33: -> ^( FUNCTION ( any )* )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:1025:36: ^( FUNCTION ( any )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_FUNCTION.nextNode(), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:1025:47: ( any )*
						while ( stream_any.hasNext() ) {
							adaptor.addChild(root_1, stream_any.nextTree());
						}
						stream_any.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 24 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1026:9: DASHMATCH
					{
					DASHMATCH241=(Token)match(input,DASHMATCH,FOLLOW_DASHMATCH_in_any2393);  
					stream_DASHMATCH.add(DASHMATCH241);

					// AST REWRITE
					// elements: DASHMATCH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1026:19: -> DASHMATCH
					{
						adaptor.addChild(root_0, stream_DASHMATCH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 25 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1027:9: LPAREN ( any )* RPAREN
					{
					LPAREN242=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_any2407);  
					stream_LPAREN.add(LPAREN242);

					// cz/vutbr/web/csskit/antlr/CSS.g:1027:16: ( any )*
					loop108:
					while (true) {
						int alt108=2;
						int LA108_0 = input.LA(1);
						if ( (LA108_0==ASTERISK||(LA108_0 >= CLASSKEYWORD && LA108_0 <= COMMA)||LA108_0==DASHMATCH||LA108_0==DIMENSION||LA108_0==EQUALS||LA108_0==EXCLAMATION||(LA108_0 >= FUNCTION && LA108_0 <= IDENT)||LA108_0==INCLUDES||LA108_0==INVALID_STRING||LA108_0==LBRACE||(LA108_0 >= LESS && LA108_0 <= LPAREN)||LA108_0==MINUS||LA108_0==NUMBER||(LA108_0 >= PERCENT && LA108_0 <= PLUS)||LA108_0==QUESTION||LA108_0==SLASH||LA108_0==STRING||(LA108_0 >= UNIRANGE && LA108_0 <= URI)) ) {
							alt108=1;
						}

						switch (alt108) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1027:16: any
							{
							pushFollow(FOLLOW_any_in_any2409);
							any243=any();
							state._fsp--;

							stream_any.add(any243.getTree());
							}
							break;

						default :
							break loop108;
						}
					}

					RPAREN244=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_any2412);  
					stream_RPAREN.add(RPAREN244);

					// AST REWRITE
					// elements: any
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1027:28: -> ^( PARENBLOCK ( any )* )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:1027:31: ^( PARENBLOCK ( any )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PARENBLOCK, "PARENBLOCK"), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:1027:44: ( any )*
						while ( stream_any.hasNext() ) {
							adaptor.addChild(root_1, stream_any.nextTree());
						}
						stream_any.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;
				case 26 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1028:9: LBRACE ( any )* RBRACE
					{
					LBRACE245=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_any2431);  
					stream_LBRACE.add(LBRACE245);

					// cz/vutbr/web/csskit/antlr/CSS.g:1028:16: ( any )*
					loop109:
					while (true) {
						int alt109=2;
						int LA109_0 = input.LA(1);
						if ( (LA109_0==ASTERISK||(LA109_0 >= CLASSKEYWORD && LA109_0 <= COMMA)||LA109_0==DASHMATCH||LA109_0==DIMENSION||LA109_0==EQUALS||LA109_0==EXCLAMATION||(LA109_0 >= FUNCTION && LA109_0 <= IDENT)||LA109_0==INCLUDES||LA109_0==INVALID_STRING||LA109_0==LBRACE||(LA109_0 >= LESS && LA109_0 <= LPAREN)||LA109_0==MINUS||LA109_0==NUMBER||(LA109_0 >= PERCENT && LA109_0 <= PLUS)||LA109_0==QUESTION||LA109_0==SLASH||LA109_0==STRING||(LA109_0 >= UNIRANGE && LA109_0 <= URI)) ) {
							alt109=1;
						}

						switch (alt109) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSS.g:1028:16: any
							{
							pushFollow(FOLLOW_any_in_any2433);
							any246=any();
							state._fsp--;

							stream_any.add(any246.getTree());
							}
							break;

						default :
							break loop109;
						}
					}

					RBRACE247=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_any2436);  
					stream_RBRACE.add(RBRACE247);

					// AST REWRITE
					// elements: any
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1028:28: -> ^( BRACEBLOCK ( any )* )
					{
						// cz/vutbr/web/csskit/antlr/CSS.g:1028:31: ^( BRACEBLOCK ( any )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BRACEBLOCK, "BRACEBLOCK"), root_1);
						// cz/vutbr/web/csskit/antlr/CSS.g:1028:44: ( any )*
						while ( stream_any.hasNext() ) {
							adaptor.addChild(root_1, stream_any.nextTree());
						}
						stream_any.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;

					}
					break;

			}

			// cz/vutbr/web/csskit/antlr/CSS.g:1029:8: ( S )*
			loop111:
			while (true) {
				int alt111=2;
				int LA111_0 = input.LA(1);
				if ( (LA111_0==S) ) {
					alt111=1;
				}

				switch (alt111) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1029:8: S
					{
					S248=(Token)match(input,S,FOLLOW_S_in_any2454);  
					stream_S.add(S248);

					}
					break;

				default :
					break loop111;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "any"


	public static class nostatement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "nostatement"
	// cz/vutbr/web/csskit/antlr/CSS.g:1032:1: nostatement : ( RCURLY -> RCURLY | SEMICOLON -> SEMICOLON | QUOT -> QUOT | APOS -> APOS ) ;
	public final CSSParser.nostatement_return nostatement() throws RecognitionException {
		CSSParser.nostatement_return retval = new CSSParser.nostatement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token RCURLY249=null;
		Token SEMICOLON250=null;
		Token QUOT251=null;
		Token APOS252=null;

		Object RCURLY249_tree=null;
		Object SEMICOLON250_tree=null;
		Object QUOT251_tree=null;
		Object APOS252_tree=null;
		RewriteRuleTokenStream stream_APOS=new RewriteRuleTokenStream(adaptor,"token APOS");
		RewriteRuleTokenStream stream_SEMICOLON=new RewriteRuleTokenStream(adaptor,"token SEMICOLON");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleTokenStream stream_QUOT=new RewriteRuleTokenStream(adaptor,"token QUOT");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1033:3: ( ( RCURLY -> RCURLY | SEMICOLON -> SEMICOLON | QUOT -> QUOT | APOS -> APOS ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:1033:5: ( RCURLY -> RCURLY | SEMICOLON -> SEMICOLON | QUOT -> QUOT | APOS -> APOS )
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1033:5: ( RCURLY -> RCURLY | SEMICOLON -> SEMICOLON | QUOT -> QUOT | APOS -> APOS )
			int alt112=4;
			switch ( input.LA(1) ) {
			case RCURLY:
				{
				alt112=1;
				}
				break;
			case SEMICOLON:
				{
				alt112=2;
				}
				break;
			case QUOT:
				{
				alt112=3;
				}
				break;
			case APOS:
				{
				alt112=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 112, 0, input);
				throw nvae;
			}
			switch (alt112) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1033:7: RCURLY
					{
					RCURLY249=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_nostatement2469);  
					stream_RCURLY.add(RCURLY249);

					// AST REWRITE
					// elements: RCURLY
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1033:14: -> RCURLY
					{
						adaptor.addChild(root_0, stream_RCURLY.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1034:9: SEMICOLON
					{
					SEMICOLON250=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_nostatement2483);  
					stream_SEMICOLON.add(SEMICOLON250);

					// AST REWRITE
					// elements: SEMICOLON
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1034:19: -> SEMICOLON
					{
						adaptor.addChild(root_0, stream_SEMICOLON.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1035:9: QUOT
					{
					QUOT251=(Token)match(input,QUOT,FOLLOW_QUOT_in_nostatement2497);  
					stream_QUOT.add(QUOT251);

					// AST REWRITE
					// elements: QUOT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1035:14: -> QUOT
					{
						adaptor.addChild(root_0, stream_QUOT.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1036:9: APOS
					{
					APOS252=(Token)match(input,APOS,FOLLOW_APOS_in_nostatement2511);  
					stream_APOS.add(APOS252);

					// AST REWRITE
					// elements: APOS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1036:14: -> APOS
					{
						adaptor.addChild(root_0, stream_APOS.nextNode());
					}


					retval.tree = root_0;

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "nostatement"


	public static class noprop_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "noprop"
	// cz/vutbr/web/csskit/antlr/CSS.g:1040:1: noprop : ( CLASSKEYWORD -> CLASSKEYWORD | NUMBER -> NUMBER | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | PLUS -> PLUS | ASTERISK -> ASTERISK | DASHMATCH -> DASHMATCH | INCLUDES -> INCLUDES | COLON -> COLON | STRING_CHAR -> STRING_CHAR | CTRL -> CTRL | INVALID_TOKEN -> INVALID_TOKEN ) ! ( S )* ;
	public final CSSParser.noprop_return noprop() throws RecognitionException {
		CSSParser.noprop_return retval = new CSSParser.noprop_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CLASSKEYWORD253=null;
		Token NUMBER254=null;
		Token COMMA255=null;
		Token GREATER256=null;
		Token LESS257=null;
		Token QUESTION258=null;
		Token PERCENT259=null;
		Token EQUALS260=null;
		Token SLASH261=null;
		Token EXCLAMATION262=null;
		Token PLUS263=null;
		Token ASTERISK264=null;
		Token DASHMATCH265=null;
		Token INCLUDES266=null;
		Token COLON267=null;
		Token STRING_CHAR268=null;
		Token CTRL269=null;
		Token INVALID_TOKEN270=null;
		Token S271=null;

		Object CLASSKEYWORD253_tree=null;
		Object NUMBER254_tree=null;
		Object COMMA255_tree=null;
		Object GREATER256_tree=null;
		Object LESS257_tree=null;
		Object QUESTION258_tree=null;
		Object PERCENT259_tree=null;
		Object EQUALS260_tree=null;
		Object SLASH261_tree=null;
		Object EXCLAMATION262_tree=null;
		Object PLUS263_tree=null;
		Object ASTERISK264_tree=null;
		Object DASHMATCH265_tree=null;
		Object INCLUDES266_tree=null;
		Object COLON267_tree=null;
		Object STRING_CHAR268_tree=null;
		Object CTRL269_tree=null;
		Object INVALID_TOKEN270_tree=null;
		Object S271_tree=null;
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_INCLUDES=new RewriteRuleTokenStream(adaptor,"token INCLUDES");
		RewriteRuleTokenStream stream_STRING_CHAR=new RewriteRuleTokenStream(adaptor,"token STRING_CHAR");
		RewriteRuleTokenStream stream_GREATER=new RewriteRuleTokenStream(adaptor,"token GREATER");
		RewriteRuleTokenStream stream_SLASH=new RewriteRuleTokenStream(adaptor,"token SLASH");
		RewriteRuleTokenStream stream_DASHMATCH=new RewriteRuleTokenStream(adaptor,"token DASHMATCH");
		RewriteRuleTokenStream stream_QUESTION=new RewriteRuleTokenStream(adaptor,"token QUESTION");
		RewriteRuleTokenStream stream_PERCENT=new RewriteRuleTokenStream(adaptor,"token PERCENT");
		RewriteRuleTokenStream stream_EXCLAMATION=new RewriteRuleTokenStream(adaptor,"token EXCLAMATION");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLASSKEYWORD=new RewriteRuleTokenStream(adaptor,"token CLASSKEYWORD");
		RewriteRuleTokenStream stream_LESS=new RewriteRuleTokenStream(adaptor,"token LESS");
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_INVALID_TOKEN=new RewriteRuleTokenStream(adaptor,"token INVALID_TOKEN");
		RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
		RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
		RewriteRuleTokenStream stream_S=new RewriteRuleTokenStream(adaptor,"token S");
		RewriteRuleTokenStream stream_ASTERISK=new RewriteRuleTokenStream(adaptor,"token ASTERISK");
		RewriteRuleTokenStream stream_CTRL=new RewriteRuleTokenStream(adaptor,"token CTRL");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1041:2: ( ( CLASSKEYWORD -> CLASSKEYWORD | NUMBER -> NUMBER | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | PLUS -> PLUS | ASTERISK -> ASTERISK | DASHMATCH -> DASHMATCH | INCLUDES -> INCLUDES | COLON -> COLON | STRING_CHAR -> STRING_CHAR | CTRL -> CTRL | INVALID_TOKEN -> INVALID_TOKEN ) ! ( S )* )
			// cz/vutbr/web/csskit/antlr/CSS.g:1041:4: ( CLASSKEYWORD -> CLASSKEYWORD | NUMBER -> NUMBER | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | PLUS -> PLUS | ASTERISK -> ASTERISK | DASHMATCH -> DASHMATCH | INCLUDES -> INCLUDES | COLON -> COLON | STRING_CHAR -> STRING_CHAR | CTRL -> CTRL | INVALID_TOKEN -> INVALID_TOKEN ) ! ( S )*
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1041:4: ( CLASSKEYWORD -> CLASSKEYWORD | NUMBER -> NUMBER | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | PLUS -> PLUS | ASTERISK -> ASTERISK | DASHMATCH -> DASHMATCH | INCLUDES -> INCLUDES | COLON -> COLON | STRING_CHAR -> STRING_CHAR | CTRL -> CTRL | INVALID_TOKEN -> INVALID_TOKEN )
			int alt113=18;
			switch ( input.LA(1) ) {
			case CLASSKEYWORD:
				{
				alt113=1;
				}
				break;
			case NUMBER:
				{
				alt113=2;
				}
				break;
			case COMMA:
				{
				alt113=3;
				}
				break;
			case GREATER:
				{
				alt113=4;
				}
				break;
			case LESS:
				{
				alt113=5;
				}
				break;
			case QUESTION:
				{
				alt113=6;
				}
				break;
			case PERCENT:
				{
				alt113=7;
				}
				break;
			case EQUALS:
				{
				alt113=8;
				}
				break;
			case SLASH:
				{
				alt113=9;
				}
				break;
			case EXCLAMATION:
				{
				alt113=10;
				}
				break;
			case PLUS:
				{
				alt113=11;
				}
				break;
			case ASTERISK:
				{
				alt113=12;
				}
				break;
			case DASHMATCH:
				{
				alt113=13;
				}
				break;
			case INCLUDES:
				{
				alt113=14;
				}
				break;
			case COLON:
				{
				alt113=15;
				}
				break;
			case STRING_CHAR:
				{
				alt113=16;
				}
				break;
			case CTRL:
				{
				alt113=17;
				}
				break;
			case INVALID_TOKEN:
				{
				alt113=18;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 113, 0, input);
				throw nvae;
			}
			switch (alt113) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1041:6: CLASSKEYWORD
					{
					CLASSKEYWORD253=(Token)match(input,CLASSKEYWORD,FOLLOW_CLASSKEYWORD_in_noprop2534);  
					stream_CLASSKEYWORD.add(CLASSKEYWORD253);

					// AST REWRITE
					// elements: CLASSKEYWORD
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1041:19: -> CLASSKEYWORD
					{
						adaptor.addChild(root_0, stream_CLASSKEYWORD.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1042:8: NUMBER
					{
					NUMBER254=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_noprop2547);  
					stream_NUMBER.add(NUMBER254);

					// AST REWRITE
					// elements: NUMBER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1042:15: -> NUMBER
					{
						adaptor.addChild(root_0, stream_NUMBER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1043:7: COMMA
					{
					COMMA255=(Token)match(input,COMMA,FOLLOW_COMMA_in_noprop2559);  
					stream_COMMA.add(COMMA255);

					// AST REWRITE
					// elements: COMMA
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1043:13: -> COMMA
					{
						adaptor.addChild(root_0, stream_COMMA.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1044:7: GREATER
					{
					GREATER256=(Token)match(input,GREATER,FOLLOW_GREATER_in_noprop2571);  
					stream_GREATER.add(GREATER256);

					// AST REWRITE
					// elements: GREATER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1044:15: -> GREATER
					{
						adaptor.addChild(root_0, stream_GREATER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1045:7: LESS
					{
					LESS257=(Token)match(input,LESS,FOLLOW_LESS_in_noprop2583);  
					stream_LESS.add(LESS257);

					// AST REWRITE
					// elements: LESS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1045:12: -> LESS
					{
						adaptor.addChild(root_0, stream_LESS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1046:7: QUESTION
					{
					QUESTION258=(Token)match(input,QUESTION,FOLLOW_QUESTION_in_noprop2595);  
					stream_QUESTION.add(QUESTION258);

					// AST REWRITE
					// elements: QUESTION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1046:16: -> QUESTION
					{
						adaptor.addChild(root_0, stream_QUESTION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1047:7: PERCENT
					{
					PERCENT259=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_noprop2607);  
					stream_PERCENT.add(PERCENT259);

					// AST REWRITE
					// elements: PERCENT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1047:15: -> PERCENT
					{
						adaptor.addChild(root_0, stream_PERCENT.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 8 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1048:7: EQUALS
					{
					EQUALS260=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_noprop2619);  
					stream_EQUALS.add(EQUALS260);

					// AST REWRITE
					// elements: EQUALS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1048:14: -> EQUALS
					{
						adaptor.addChild(root_0, stream_EQUALS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 9 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1049:7: SLASH
					{
					SLASH261=(Token)match(input,SLASH,FOLLOW_SLASH_in_noprop2631);  
					stream_SLASH.add(SLASH261);

					// AST REWRITE
					// elements: SLASH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1049:13: -> SLASH
					{
						adaptor.addChild(root_0, stream_SLASH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 10 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1050:7: EXCLAMATION
					{
					EXCLAMATION262=(Token)match(input,EXCLAMATION,FOLLOW_EXCLAMATION_in_noprop2643);  
					stream_EXCLAMATION.add(EXCLAMATION262);

					// AST REWRITE
					// elements: EXCLAMATION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1050:19: -> EXCLAMATION
					{
						adaptor.addChild(root_0, stream_EXCLAMATION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 11 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1051:7: PLUS
					{
					PLUS263=(Token)match(input,PLUS,FOLLOW_PLUS_in_noprop2655);  
					stream_PLUS.add(PLUS263);

					// AST REWRITE
					// elements: PLUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1051:12: -> PLUS
					{
						adaptor.addChild(root_0, stream_PLUS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 12 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1052:7: ASTERISK
					{
					ASTERISK264=(Token)match(input,ASTERISK,FOLLOW_ASTERISK_in_noprop2667);  
					stream_ASTERISK.add(ASTERISK264);

					// AST REWRITE
					// elements: ASTERISK
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1052:16: -> ASTERISK
					{
						adaptor.addChild(root_0, stream_ASTERISK.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 13 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1053:7: DASHMATCH
					{
					DASHMATCH265=(Token)match(input,DASHMATCH,FOLLOW_DASHMATCH_in_noprop2682);  
					stream_DASHMATCH.add(DASHMATCH265);

					// AST REWRITE
					// elements: DASHMATCH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1053:17: -> DASHMATCH
					{
						adaptor.addChild(root_0, stream_DASHMATCH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 14 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1054:7: INCLUDES
					{
					INCLUDES266=(Token)match(input,INCLUDES,FOLLOW_INCLUDES_in_noprop2694);  
					stream_INCLUDES.add(INCLUDES266);

					// AST REWRITE
					// elements: INCLUDES
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1054:16: -> INCLUDES
					{
						adaptor.addChild(root_0, stream_INCLUDES.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 15 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1055:7: COLON
					{
					COLON267=(Token)match(input,COLON,FOLLOW_COLON_in_noprop2706);  
					stream_COLON.add(COLON267);

					// AST REWRITE
					// elements: COLON
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1055:13: -> COLON
					{
						adaptor.addChild(root_0, stream_COLON.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 16 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1056:7: STRING_CHAR
					{
					STRING_CHAR268=(Token)match(input,STRING_CHAR,FOLLOW_STRING_CHAR_in_noprop2718);  
					stream_STRING_CHAR.add(STRING_CHAR268);

					// AST REWRITE
					// elements: STRING_CHAR
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1056:19: -> STRING_CHAR
					{
						adaptor.addChild(root_0, stream_STRING_CHAR.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 17 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1057:8: CTRL
					{
					CTRL269=(Token)match(input,CTRL,FOLLOW_CTRL_in_noprop2731);  
					stream_CTRL.add(CTRL269);

					// AST REWRITE
					// elements: CTRL
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1057:13: -> CTRL
					{
						adaptor.addChild(root_0, stream_CTRL.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 18 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1058:7: INVALID_TOKEN
					{
					INVALID_TOKEN270=(Token)match(input,INVALID_TOKEN,FOLLOW_INVALID_TOKEN_in_noprop2743);  
					stream_INVALID_TOKEN.add(INVALID_TOKEN270);

					// AST REWRITE
					// elements: INVALID_TOKEN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1058:21: -> INVALID_TOKEN
					{
						adaptor.addChild(root_0, stream_INVALID_TOKEN.nextNode());
					}


					retval.tree = root_0;

					}
					break;

			}

			// cz/vutbr/web/csskit/antlr/CSS.g:1059:8: ( S )*
			loop114:
			while (true) {
				int alt114=2;
				int LA114_0 = input.LA(1);
				if ( (LA114_0==S) ) {
					alt114=1;
				}

				switch (alt114) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1059:8: S
					{
					S271=(Token)match(input,S,FOLLOW_S_in_noprop2756);  
					stream_S.add(S271);

					}
					break;

				default :
					break loop114;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "noprop"


	public static class norule_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "norule"
	// cz/vutbr/web/csskit/antlr/CSS.g:1062:1: norule : ( NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | DASHMATCH -> DASHMATCH | RPAREN -> RPAREN | CTRL -> CTRL | '#' | '^' | '&' ) ;
	public final CSSParser.norule_return norule() throws RecognitionException {
		CSSParser.norule_return retval = new CSSParser.norule_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NUMBER272=null;
		Token PERCENTAGE273=null;
		Token DIMENSION274=null;
		Token URI276=null;
		Token UNIRANGE277=null;
		Token INCLUDES278=null;
		Token COMMA279=null;
		Token GREATER280=null;
		Token LESS281=null;
		Token QUESTION282=null;
		Token PERCENT283=null;
		Token EQUALS284=null;
		Token SLASH285=null;
		Token EXCLAMATION286=null;
		Token MINUS287=null;
		Token PLUS288=null;
		Token DASHMATCH289=null;
		Token RPAREN290=null;
		Token CTRL291=null;
		Token char_literal292=null;
		Token char_literal293=null;
		Token char_literal294=null;
		ParserRuleReturnScope string275 =null;

		Object NUMBER272_tree=null;
		Object PERCENTAGE273_tree=null;
		Object DIMENSION274_tree=null;
		Object URI276_tree=null;
		Object UNIRANGE277_tree=null;
		Object INCLUDES278_tree=null;
		Object COMMA279_tree=null;
		Object GREATER280_tree=null;
		Object LESS281_tree=null;
		Object QUESTION282_tree=null;
		Object PERCENT283_tree=null;
		Object EQUALS284_tree=null;
		Object SLASH285_tree=null;
		Object EXCLAMATION286_tree=null;
		Object MINUS287_tree=null;
		Object PLUS288_tree=null;
		Object DASHMATCH289_tree=null;
		Object RPAREN290_tree=null;
		Object CTRL291_tree=null;
		Object char_literal292_tree=null;
		Object char_literal293_tree=null;
		Object char_literal294_tree=null;
		RewriteRuleTokenStream stream_INCLUDES=new RewriteRuleTokenStream(adaptor,"token INCLUDES");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_SLASH=new RewriteRuleTokenStream(adaptor,"token SLASH");
		RewriteRuleTokenStream stream_GREATER=new RewriteRuleTokenStream(adaptor,"token GREATER");
		RewriteRuleTokenStream stream_DASHMATCH=new RewriteRuleTokenStream(adaptor,"token DASHMATCH");
		RewriteRuleTokenStream stream_EXCLAMATION=new RewriteRuleTokenStream(adaptor,"token EXCLAMATION");
		RewriteRuleTokenStream stream_QUESTION=new RewriteRuleTokenStream(adaptor,"token QUESTION");
		RewriteRuleTokenStream stream_PERCENT=new RewriteRuleTokenStream(adaptor,"token PERCENT");
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_LESS=new RewriteRuleTokenStream(adaptor,"token LESS");
		RewriteRuleTokenStream stream_UNIRANGE=new RewriteRuleTokenStream(adaptor,"token UNIRANGE");
		RewriteRuleTokenStream stream_DIMENSION=new RewriteRuleTokenStream(adaptor,"token DIMENSION");
		RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
		RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
		RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");
		RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
		RewriteRuleTokenStream stream_102=new RewriteRuleTokenStream(adaptor,"token 102");
		RewriteRuleTokenStream stream_PERCENTAGE=new RewriteRuleTokenStream(adaptor,"token PERCENTAGE");
		RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
		RewriteRuleTokenStream stream_URI=new RewriteRuleTokenStream(adaptor,"token URI");
		RewriteRuleTokenStream stream_CTRL=new RewriteRuleTokenStream(adaptor,"token CTRL");
		RewriteRuleSubtreeStream stream_string=new RewriteRuleSubtreeStream(adaptor,"rule string");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1063:3: ( ( NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | DASHMATCH -> DASHMATCH | RPAREN -> RPAREN | CTRL -> CTRL | '#' | '^' | '&' ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:1063:5: ( NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | DASHMATCH -> DASHMATCH | RPAREN -> RPAREN | CTRL -> CTRL | '#' | '^' | '&' )
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1063:5: ( NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | COMMA -> COMMA | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | DASHMATCH -> DASHMATCH | RPAREN -> RPAREN | CTRL -> CTRL | '#' | '^' | '&' )
			int alt115=23;
			switch ( input.LA(1) ) {
			case NUMBER:
				{
				alt115=1;
				}
				break;
			case PERCENTAGE:
				{
				alt115=2;
				}
				break;
			case DIMENSION:
				{
				alt115=3;
				}
				break;
			case INVALID_STRING:
			case STRING:
				{
				alt115=4;
				}
				break;
			case URI:
				{
				alt115=5;
				}
				break;
			case UNIRANGE:
				{
				alt115=6;
				}
				break;
			case INCLUDES:
				{
				alt115=7;
				}
				break;
			case COMMA:
				{
				alt115=8;
				}
				break;
			case GREATER:
				{
				alt115=9;
				}
				break;
			case LESS:
				{
				alt115=10;
				}
				break;
			case QUESTION:
				{
				alt115=11;
				}
				break;
			case PERCENT:
				{
				alt115=12;
				}
				break;
			case EQUALS:
				{
				alt115=13;
				}
				break;
			case SLASH:
				{
				alt115=14;
				}
				break;
			case EXCLAMATION:
				{
				alt115=15;
				}
				break;
			case MINUS:
				{
				alt115=16;
				}
				break;
			case PLUS:
				{
				alt115=17;
				}
				break;
			case DASHMATCH:
				{
				alt115=18;
				}
				break;
			case RPAREN:
				{
				alt115=19;
				}
				break;
			case CTRL:
				{
				alt115=20;
				}
				break;
			case 101:
				{
				alt115=21;
				}
				break;
			case 103:
				{
				alt115=22;
				}
				break;
			case 102:
				{
				alt115=23;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 115, 0, input);
				throw nvae;
			}
			switch (alt115) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1063:7: NUMBER
					{
					NUMBER272=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_norule2771);  
					stream_NUMBER.add(NUMBER272);

					// AST REWRITE
					// elements: NUMBER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1063:14: -> NUMBER
					{
						adaptor.addChild(root_0, stream_NUMBER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1064:8: PERCENTAGE
					{
					PERCENTAGE273=(Token)match(input,PERCENTAGE,FOLLOW_PERCENTAGE_in_norule2784);  
					stream_PERCENTAGE.add(PERCENTAGE273);

					// AST REWRITE
					// elements: PERCENTAGE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1064:19: -> PERCENTAGE
					{
						adaptor.addChild(root_0, stream_PERCENTAGE.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1065:8: DIMENSION
					{
					DIMENSION274=(Token)match(input,DIMENSION,FOLLOW_DIMENSION_in_norule2796);  
					stream_DIMENSION.add(DIMENSION274);

					// AST REWRITE
					// elements: DIMENSION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1065:18: -> DIMENSION
					{
						adaptor.addChild(root_0, stream_DIMENSION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1066:8: string
					{
					pushFollow(FOLLOW_string_in_norule2809);
					string275=string();
					state._fsp--;

					stream_string.add(string275.getTree());
					// AST REWRITE
					// elements: string
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1066:15: -> string
					{
						adaptor.addChild(root_0, stream_string.nextTree());
					}


					retval.tree = root_0;

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1067:9: URI
					{
					URI276=(Token)match(input,URI,FOLLOW_URI_in_norule2823);  
					stream_URI.add(URI276);

					// AST REWRITE
					// elements: URI
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1067:16: -> URI
					{
						adaptor.addChild(root_0, stream_URI.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1068:9: UNIRANGE
					{
					UNIRANGE277=(Token)match(input,UNIRANGE,FOLLOW_UNIRANGE_in_norule2840);  
					stream_UNIRANGE.add(UNIRANGE277);

					// AST REWRITE
					// elements: UNIRANGE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1068:18: -> UNIRANGE
					{
						adaptor.addChild(root_0, stream_UNIRANGE.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1069:9: INCLUDES
					{
					INCLUDES278=(Token)match(input,INCLUDES,FOLLOW_INCLUDES_in_norule2854);  
					stream_INCLUDES.add(INCLUDES278);

					// AST REWRITE
					// elements: INCLUDES
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1069:18: -> INCLUDES
					{
						adaptor.addChild(root_0, stream_INCLUDES.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 8 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1070:9: COMMA
					{
					COMMA279=(Token)match(input,COMMA,FOLLOW_COMMA_in_norule2868);  
					stream_COMMA.add(COMMA279);

					// AST REWRITE
					// elements: COMMA
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1070:15: -> COMMA
					{
						adaptor.addChild(root_0, stream_COMMA.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 9 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1071:9: GREATER
					{
					GREATER280=(Token)match(input,GREATER,FOLLOW_GREATER_in_norule2882);  
					stream_GREATER.add(GREATER280);

					// AST REWRITE
					// elements: GREATER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1071:17: -> GREATER
					{
						adaptor.addChild(root_0, stream_GREATER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 10 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1072:9: LESS
					{
					LESS281=(Token)match(input,LESS,FOLLOW_LESS_in_norule2896);  
					stream_LESS.add(LESS281);

					// AST REWRITE
					// elements: LESS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1072:14: -> LESS
					{
						adaptor.addChild(root_0, stream_LESS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 11 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1073:9: QUESTION
					{
					QUESTION282=(Token)match(input,QUESTION,FOLLOW_QUESTION_in_norule2910);  
					stream_QUESTION.add(QUESTION282);

					// AST REWRITE
					// elements: QUESTION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1073:18: -> QUESTION
					{
						adaptor.addChild(root_0, stream_QUESTION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 12 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1074:9: PERCENT
					{
					PERCENT283=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_norule2924);  
					stream_PERCENT.add(PERCENT283);

					// AST REWRITE
					// elements: PERCENT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1074:17: -> PERCENT
					{
						adaptor.addChild(root_0, stream_PERCENT.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 13 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1075:9: EQUALS
					{
					EQUALS284=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_norule2938);  
					stream_EQUALS.add(EQUALS284);

					// AST REWRITE
					// elements: EQUALS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1075:16: -> EQUALS
					{
						adaptor.addChild(root_0, stream_EQUALS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 14 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1076:9: SLASH
					{
					SLASH285=(Token)match(input,SLASH,FOLLOW_SLASH_in_norule2952);  
					stream_SLASH.add(SLASH285);

					// AST REWRITE
					// elements: SLASH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1076:15: -> SLASH
					{
						adaptor.addChild(root_0, stream_SLASH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 15 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1077:9: EXCLAMATION
					{
					EXCLAMATION286=(Token)match(input,EXCLAMATION,FOLLOW_EXCLAMATION_in_norule2966);  
					stream_EXCLAMATION.add(EXCLAMATION286);

					// AST REWRITE
					// elements: EXCLAMATION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1077:21: -> EXCLAMATION
					{
						adaptor.addChild(root_0, stream_EXCLAMATION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 16 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1078:8: MINUS
					{
					MINUS287=(Token)match(input,MINUS,FOLLOW_MINUS_in_norule2979);  
					stream_MINUS.add(MINUS287);

					// AST REWRITE
					// elements: MINUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1078:14: -> MINUS
					{
						adaptor.addChild(root_0, stream_MINUS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 17 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1079:8: PLUS
					{
					PLUS288=(Token)match(input,PLUS,FOLLOW_PLUS_in_norule2992);  
					stream_PLUS.add(PLUS288);

					// AST REWRITE
					// elements: PLUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1079:13: -> PLUS
					{
						adaptor.addChild(root_0, stream_PLUS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 18 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1080:9: DASHMATCH
					{
					DASHMATCH289=(Token)match(input,DASHMATCH,FOLLOW_DASHMATCH_in_norule3006);  
					stream_DASHMATCH.add(DASHMATCH289);

					// AST REWRITE
					// elements: DASHMATCH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1080:19: -> DASHMATCH
					{
						adaptor.addChild(root_0, stream_DASHMATCH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 19 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1081:9: RPAREN
					{
					RPAREN290=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_norule3020);  
					stream_RPAREN.add(RPAREN290);

					// AST REWRITE
					// elements: RPAREN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1081:16: -> RPAREN
					{
						adaptor.addChild(root_0, stream_RPAREN.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 20 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1082:9: CTRL
					{
					CTRL291=(Token)match(input,CTRL,FOLLOW_CTRL_in_norule3034);  
					stream_CTRL.add(CTRL291);

					// AST REWRITE
					// elements: CTRL
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1082:14: -> CTRL
					{
						adaptor.addChild(root_0, stream_CTRL.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 21 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1083:9: '#'
					{
					char_literal292=(Token)match(input,101,FOLLOW_101_in_norule3048);  
					stream_101.add(char_literal292);

					}
					break;
				case 22 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1084:9: '^'
					{
					char_literal293=(Token)match(input,103,FOLLOW_103_in_norule3059);  
					stream_103.add(char_literal293);

					}
					break;
				case 23 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1085:9: '&'
					{
					char_literal294=(Token)match(input,102,FOLLOW_102_in_norule3069);  
					stream_102.add(char_literal294);

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "norule"


	public static class nomediaquery_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "nomediaquery"
	// cz/vutbr/web/csskit/antlr/CSS.g:1089:1: nomediaquery : ( NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | DASHMATCH -> DASHMATCH | RPAREN -> RPAREN | CTRL -> CTRL | COLON -> COLON | ASTERISK -> ASTERISK | FUNCTION -> FUNCTION | '#' | '^' | '&' ) ;
	public final CSSParser.nomediaquery_return nomediaquery() throws RecognitionException {
		CSSParser.nomediaquery_return retval = new CSSParser.nomediaquery_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NUMBER295=null;
		Token PERCENTAGE296=null;
		Token DIMENSION297=null;
		Token URI299=null;
		Token UNIRANGE300=null;
		Token INCLUDES301=null;
		Token GREATER302=null;
		Token LESS303=null;
		Token QUESTION304=null;
		Token PERCENT305=null;
		Token EQUALS306=null;
		Token SLASH307=null;
		Token EXCLAMATION308=null;
		Token MINUS309=null;
		Token PLUS310=null;
		Token DASHMATCH311=null;
		Token RPAREN312=null;
		Token CTRL313=null;
		Token COLON314=null;
		Token ASTERISK315=null;
		Token FUNCTION316=null;
		Token char_literal317=null;
		Token char_literal318=null;
		Token char_literal319=null;
		ParserRuleReturnScope string298 =null;

		Object NUMBER295_tree=null;
		Object PERCENTAGE296_tree=null;
		Object DIMENSION297_tree=null;
		Object URI299_tree=null;
		Object UNIRANGE300_tree=null;
		Object INCLUDES301_tree=null;
		Object GREATER302_tree=null;
		Object LESS303_tree=null;
		Object QUESTION304_tree=null;
		Object PERCENT305_tree=null;
		Object EQUALS306_tree=null;
		Object SLASH307_tree=null;
		Object EXCLAMATION308_tree=null;
		Object MINUS309_tree=null;
		Object PLUS310_tree=null;
		Object DASHMATCH311_tree=null;
		Object RPAREN312_tree=null;
		Object CTRL313_tree=null;
		Object COLON314_tree=null;
		Object ASTERISK315_tree=null;
		Object FUNCTION316_tree=null;
		Object char_literal317_tree=null;
		Object char_literal318_tree=null;
		Object char_literal319_tree=null;
		RewriteRuleTokenStream stream_FUNCTION=new RewriteRuleTokenStream(adaptor,"token FUNCTION");
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_INCLUDES=new RewriteRuleTokenStream(adaptor,"token INCLUDES");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_GREATER=new RewriteRuleTokenStream(adaptor,"token GREATER");
		RewriteRuleTokenStream stream_SLASH=new RewriteRuleTokenStream(adaptor,"token SLASH");
		RewriteRuleTokenStream stream_DASHMATCH=new RewriteRuleTokenStream(adaptor,"token DASHMATCH");
		RewriteRuleTokenStream stream_EXCLAMATION=new RewriteRuleTokenStream(adaptor,"token EXCLAMATION");
		RewriteRuleTokenStream stream_QUESTION=new RewriteRuleTokenStream(adaptor,"token QUESTION");
		RewriteRuleTokenStream stream_PERCENT=new RewriteRuleTokenStream(adaptor,"token PERCENT");
		RewriteRuleTokenStream stream_LESS=new RewriteRuleTokenStream(adaptor,"token LESS");
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_UNIRANGE=new RewriteRuleTokenStream(adaptor,"token UNIRANGE");
		RewriteRuleTokenStream stream_DIMENSION=new RewriteRuleTokenStream(adaptor,"token DIMENSION");
		RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
		RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
		RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");
		RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
		RewriteRuleTokenStream stream_102=new RewriteRuleTokenStream(adaptor,"token 102");
		RewriteRuleTokenStream stream_PERCENTAGE=new RewriteRuleTokenStream(adaptor,"token PERCENTAGE");
		RewriteRuleTokenStream stream_ASTERISK=new RewriteRuleTokenStream(adaptor,"token ASTERISK");
		RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
		RewriteRuleTokenStream stream_URI=new RewriteRuleTokenStream(adaptor,"token URI");
		RewriteRuleTokenStream stream_CTRL=new RewriteRuleTokenStream(adaptor,"token CTRL");
		RewriteRuleSubtreeStream stream_string=new RewriteRuleSubtreeStream(adaptor,"rule string");

		try {
			// cz/vutbr/web/csskit/antlr/CSS.g:1090:3: ( ( NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | DASHMATCH -> DASHMATCH | RPAREN -> RPAREN | CTRL -> CTRL | COLON -> COLON | ASTERISK -> ASTERISK | FUNCTION -> FUNCTION | '#' | '^' | '&' ) )
			// cz/vutbr/web/csskit/antlr/CSS.g:1090:5: ( NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | DASHMATCH -> DASHMATCH | RPAREN -> RPAREN | CTRL -> CTRL | COLON -> COLON | ASTERISK -> ASTERISK | FUNCTION -> FUNCTION | '#' | '^' | '&' )
			{
			// cz/vutbr/web/csskit/antlr/CSS.g:1090:5: ( NUMBER -> NUMBER | PERCENTAGE -> PERCENTAGE | DIMENSION -> DIMENSION | string -> string | URI -> URI | UNIRANGE -> UNIRANGE | INCLUDES -> INCLUDES | GREATER -> GREATER | LESS -> LESS | QUESTION -> QUESTION | PERCENT -> PERCENT | EQUALS -> EQUALS | SLASH -> SLASH | EXCLAMATION -> EXCLAMATION | MINUS -> MINUS | PLUS -> PLUS | DASHMATCH -> DASHMATCH | RPAREN -> RPAREN | CTRL -> CTRL | COLON -> COLON | ASTERISK -> ASTERISK | FUNCTION -> FUNCTION | '#' | '^' | '&' )
			int alt116=25;
			switch ( input.LA(1) ) {
			case NUMBER:
				{
				alt116=1;
				}
				break;
			case PERCENTAGE:
				{
				alt116=2;
				}
				break;
			case DIMENSION:
				{
				alt116=3;
				}
				break;
			case INVALID_STRING:
			case STRING:
				{
				alt116=4;
				}
				break;
			case URI:
				{
				alt116=5;
				}
				break;
			case UNIRANGE:
				{
				alt116=6;
				}
				break;
			case INCLUDES:
				{
				alt116=7;
				}
				break;
			case GREATER:
				{
				alt116=8;
				}
				break;
			case LESS:
				{
				alt116=9;
				}
				break;
			case QUESTION:
				{
				alt116=10;
				}
				break;
			case PERCENT:
				{
				alt116=11;
				}
				break;
			case EQUALS:
				{
				alt116=12;
				}
				break;
			case SLASH:
				{
				alt116=13;
				}
				break;
			case EXCLAMATION:
				{
				alt116=14;
				}
				break;
			case MINUS:
				{
				alt116=15;
				}
				break;
			case PLUS:
				{
				alt116=16;
				}
				break;
			case DASHMATCH:
				{
				alt116=17;
				}
				break;
			case RPAREN:
				{
				alt116=18;
				}
				break;
			case CTRL:
				{
				alt116=19;
				}
				break;
			case COLON:
				{
				alt116=20;
				}
				break;
			case ASTERISK:
				{
				alt116=21;
				}
				break;
			case FUNCTION:
				{
				alt116=22;
				}
				break;
			case 101:
				{
				alt116=23;
				}
				break;
			case 103:
				{
				alt116=24;
				}
				break;
			case 102:
				{
				alt116=25;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 116, 0, input);
				throw nvae;
			}
			switch (alt116) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1090:7: NUMBER
					{
					NUMBER295=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_nomediaquery3093);  
					stream_NUMBER.add(NUMBER295);

					// AST REWRITE
					// elements: NUMBER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1090:14: -> NUMBER
					{
						adaptor.addChild(root_0, stream_NUMBER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1091:9: PERCENTAGE
					{
					PERCENTAGE296=(Token)match(input,PERCENTAGE,FOLLOW_PERCENTAGE_in_nomediaquery3107);  
					stream_PERCENTAGE.add(PERCENTAGE296);

					// AST REWRITE
					// elements: PERCENTAGE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1091:20: -> PERCENTAGE
					{
						adaptor.addChild(root_0, stream_PERCENTAGE.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1092:9: DIMENSION
					{
					DIMENSION297=(Token)match(input,DIMENSION,FOLLOW_DIMENSION_in_nomediaquery3120);  
					stream_DIMENSION.add(DIMENSION297);

					// AST REWRITE
					// elements: DIMENSION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1092:19: -> DIMENSION
					{
						adaptor.addChild(root_0, stream_DIMENSION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1093:9: string
					{
					pushFollow(FOLLOW_string_in_nomediaquery3134);
					string298=string();
					state._fsp--;

					stream_string.add(string298.getTree());
					// AST REWRITE
					// elements: string
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1093:16: -> string
					{
						adaptor.addChild(root_0, stream_string.nextTree());
					}


					retval.tree = root_0;

					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1094:9: URI
					{
					URI299=(Token)match(input,URI,FOLLOW_URI_in_nomediaquery3148);  
					stream_URI.add(URI299);

					// AST REWRITE
					// elements: URI
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1094:16: -> URI
					{
						adaptor.addChild(root_0, stream_URI.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1095:9: UNIRANGE
					{
					UNIRANGE300=(Token)match(input,UNIRANGE,FOLLOW_UNIRANGE_in_nomediaquery3165);  
					stream_UNIRANGE.add(UNIRANGE300);

					// AST REWRITE
					// elements: UNIRANGE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1095:18: -> UNIRANGE
					{
						adaptor.addChild(root_0, stream_UNIRANGE.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1096:9: INCLUDES
					{
					INCLUDES301=(Token)match(input,INCLUDES,FOLLOW_INCLUDES_in_nomediaquery3179);  
					stream_INCLUDES.add(INCLUDES301);

					// AST REWRITE
					// elements: INCLUDES
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1096:18: -> INCLUDES
					{
						adaptor.addChild(root_0, stream_INCLUDES.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 8 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1097:9: GREATER
					{
					GREATER302=(Token)match(input,GREATER,FOLLOW_GREATER_in_nomediaquery3193);  
					stream_GREATER.add(GREATER302);

					// AST REWRITE
					// elements: GREATER
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1097:17: -> GREATER
					{
						adaptor.addChild(root_0, stream_GREATER.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 9 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1098:9: LESS
					{
					LESS303=(Token)match(input,LESS,FOLLOW_LESS_in_nomediaquery3207);  
					stream_LESS.add(LESS303);

					// AST REWRITE
					// elements: LESS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1098:14: -> LESS
					{
						adaptor.addChild(root_0, stream_LESS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 10 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1099:9: QUESTION
					{
					QUESTION304=(Token)match(input,QUESTION,FOLLOW_QUESTION_in_nomediaquery3221);  
					stream_QUESTION.add(QUESTION304);

					// AST REWRITE
					// elements: QUESTION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1099:18: -> QUESTION
					{
						adaptor.addChild(root_0, stream_QUESTION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 11 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1100:9: PERCENT
					{
					PERCENT305=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_nomediaquery3235);  
					stream_PERCENT.add(PERCENT305);

					// AST REWRITE
					// elements: PERCENT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1100:17: -> PERCENT
					{
						adaptor.addChild(root_0, stream_PERCENT.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 12 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1101:9: EQUALS
					{
					EQUALS306=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_nomediaquery3249);  
					stream_EQUALS.add(EQUALS306);

					// AST REWRITE
					// elements: EQUALS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1101:16: -> EQUALS
					{
						adaptor.addChild(root_0, stream_EQUALS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 13 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1102:9: SLASH
					{
					SLASH307=(Token)match(input,SLASH,FOLLOW_SLASH_in_nomediaquery3263);  
					stream_SLASH.add(SLASH307);

					// AST REWRITE
					// elements: SLASH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1102:15: -> SLASH
					{
						adaptor.addChild(root_0, stream_SLASH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 14 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1103:9: EXCLAMATION
					{
					EXCLAMATION308=(Token)match(input,EXCLAMATION,FOLLOW_EXCLAMATION_in_nomediaquery3277);  
					stream_EXCLAMATION.add(EXCLAMATION308);

					// AST REWRITE
					// elements: EXCLAMATION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1103:21: -> EXCLAMATION
					{
						adaptor.addChild(root_0, stream_EXCLAMATION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 15 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1104:9: MINUS
					{
					MINUS309=(Token)match(input,MINUS,FOLLOW_MINUS_in_nomediaquery3291);  
					stream_MINUS.add(MINUS309);

					// AST REWRITE
					// elements: MINUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1104:15: -> MINUS
					{
						adaptor.addChild(root_0, stream_MINUS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 16 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1105:9: PLUS
					{
					PLUS310=(Token)match(input,PLUS,FOLLOW_PLUS_in_nomediaquery3305);  
					stream_PLUS.add(PLUS310);

					// AST REWRITE
					// elements: PLUS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1105:14: -> PLUS
					{
						adaptor.addChild(root_0, stream_PLUS.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 17 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1106:9: DASHMATCH
					{
					DASHMATCH311=(Token)match(input,DASHMATCH,FOLLOW_DASHMATCH_in_nomediaquery3319);  
					stream_DASHMATCH.add(DASHMATCH311);

					// AST REWRITE
					// elements: DASHMATCH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1106:19: -> DASHMATCH
					{
						adaptor.addChild(root_0, stream_DASHMATCH.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 18 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1107:9: RPAREN
					{
					RPAREN312=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nomediaquery3333);  
					stream_RPAREN.add(RPAREN312);

					// AST REWRITE
					// elements: RPAREN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1107:16: -> RPAREN
					{
						adaptor.addChild(root_0, stream_RPAREN.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 19 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1108:9: CTRL
					{
					CTRL313=(Token)match(input,CTRL,FOLLOW_CTRL_in_nomediaquery3347);  
					stream_CTRL.add(CTRL313);

					// AST REWRITE
					// elements: CTRL
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1108:14: -> CTRL
					{
						adaptor.addChild(root_0, stream_CTRL.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 20 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1109:9: COLON
					{
					COLON314=(Token)match(input,COLON,FOLLOW_COLON_in_nomediaquery3361);  
					stream_COLON.add(COLON314);

					// AST REWRITE
					// elements: COLON
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1109:15: -> COLON
					{
						adaptor.addChild(root_0, stream_COLON.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 21 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1110:9: ASTERISK
					{
					ASTERISK315=(Token)match(input,ASTERISK,FOLLOW_ASTERISK_in_nomediaquery3375);  
					stream_ASTERISK.add(ASTERISK315);

					// AST REWRITE
					// elements: ASTERISK
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1110:18: -> ASTERISK
					{
						adaptor.addChild(root_0, stream_ASTERISK.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 22 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1111:9: FUNCTION
					{
					FUNCTION316=(Token)match(input,FUNCTION,FOLLOW_FUNCTION_in_nomediaquery3389);  
					stream_FUNCTION.add(FUNCTION316);

					// AST REWRITE
					// elements: FUNCTION
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 1111:18: -> FUNCTION
					{
						adaptor.addChild(root_0, stream_FUNCTION.nextNode());
					}


					retval.tree = root_0;

					}
					break;
				case 23 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1112:9: '#'
					{
					char_literal317=(Token)match(input,101,FOLLOW_101_in_nomediaquery3403);  
					stream_101.add(char_literal317);

					}
					break;
				case 24 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1113:9: '^'
					{
					char_literal318=(Token)match(input,103,FOLLOW_103_in_nomediaquery3414);  
					stream_103.add(char_literal318);

					}
					break;
				case 25 :
					// cz/vutbr/web/csskit/antlr/CSS.g:1114:9: '&'
					{
					char_literal319=(Token)match(input,102,FOLLOW_102_in_nomediaquery3424);  
					stream_102.add(char_literal319);

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "nomediaquery"

	// Delegated rules



	public static final BitSet FOLLOW_S_in_inlinestyle203 = new BitSet(new long[]{0x10D00228A0938040L,0x00000000022A0944L});
	public static final BitSet FOLLOW_declarations_in_inlinestyle208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_inlineset_in_inlinestyle228 = new BitSet(new long[]{0x0040000000010002L});
	public static final BitSet FOLLOW_CDO_in_stylesheet256 = new BitSet(new long[]{0x14AA02BAA493B962L,0x000000E4612AD9D4L});
	public static final BitSet FOLLOW_CDC_in_stylesheet260 = new BitSet(new long[]{0x14AA02BAA493B962L,0x000000E4612AD9D4L});
	public static final BitSet FOLLOW_S_in_stylesheet264 = new BitSet(new long[]{0x14AA02BAA493B962L,0x000000E4612AD9D4L});
	public static final BitSet FOLLOW_nostatement_in_stylesheet268 = new BitSet(new long[]{0x14AA02BAA493B962L,0x000000E4612AD9D4L});
	public static final BitSet FOLLOW_statement_in_stylesheet272 = new BitSet(new long[]{0x14AA02BAA493B962L,0x000000E4612AD9D4L});
	public static final BitSet FOLLOW_ruleset_in_statement302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_atstatement_in_statement306 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CHARSET_in_atstatement317 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IMPORT_in_atstatement322 = new BitSet(new long[]{0x0000000000000000L,0x0000000041020000L});
	public static final BitSet FOLLOW_S_in_atstatement324 = new BitSet(new long[]{0x0000000000000000L,0x0000000041020000L});
	public static final BitSet FOLLOW_import_uri_in_atstatement327 = new BitSet(new long[]{0x1188022CA4910040L,0x000000E0612A89C4L});
	public static final BitSet FOLLOW_S_in_atstatement329 = new BitSet(new long[]{0x1188022CA4910040L,0x000000E0612A89C4L});
	public static final BitSet FOLLOW_media_in_atstatement332 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_SEMICOLON_in_atstatement335 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_page_in_atstatement356 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VIEWPORT_in_atstatement362 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_atstatement364 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_LCURLY_in_atstatement371 = new BitSet(new long[]{0x10900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_S_in_atstatement373 = new BitSet(new long[]{0x10900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_declarations_in_atstatement376 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_RCURLY_in_atstatement382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FONTFACE_in_atstatement395 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_atstatement397 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_LCURLY_in_atstatement403 = new BitSet(new long[]{0x10900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_S_in_atstatement405 = new BitSet(new long[]{0x10900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_declarations_in_atstatement408 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_RCURLY_in_atstatement413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MEDIA_in_atstatement426 = new BitSet(new long[]{0x11C8022CA4910040L,0x000000E0612289C4L});
	public static final BitSet FOLLOW_S_in_atstatement428 = new BitSet(new long[]{0x11C8022CA4910040L,0x000000E0612289C4L});
	public static final BitSet FOLLOW_media_in_atstatement431 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_LCURLY_in_atstatement437 = new BitSet(new long[]{0x14AA02BAA493A140L,0x000000E46122C9D4L});
	public static final BitSet FOLLOW_S_in_atstatement439 = new BitSet(new long[]{0x14AA02BAA493A140L,0x000000E46122C9D4L});
	public static final BitSet FOLLOW_media_rule_in_atstatement443 = new BitSet(new long[]{0x14AA02BAA493A140L,0x000000E46122C9D4L});
	public static final BitSet FOLLOW_S_in_atstatement445 = new BitSet(new long[]{0x14AA02BAA493A140L,0x000000E46122C9D4L});
	public static final BitSet FOLLOW_RCURLY_in_atstatement450 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ATKEYWORD_in_atstatement468 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_atstatement470 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_LCURLY_in_atstatement473 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612049C4L});
	public static final BitSet FOLLOW_any_in_atstatement475 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612049C4L});
	public static final BitSet FOLLOW_RCURLY_in_atstatement478 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PAGE_in_page519 = new BitSet(new long[]{0x0040002000010000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_page521 = new BitSet(new long[]{0x0040002000010000L,0x0000000000020000L});
	public static final BitSet FOLLOW_IDENT_in_page527 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_IDENT_in_page531 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_page_pseudo_in_page533 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_page_pseudo_in_page537 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_page540 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_LCURLY_in_page548 = new BitSet(new long[]{0x12900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_S_in_page550 = new BitSet(new long[]{0x12900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_declarations_in_page555 = new BitSet(new long[]{0x0200000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_margin_rule_in_page557 = new BitSet(new long[]{0x0200000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_RCURLY_in_page562 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pseudocolon_in_page_pseudo596 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_IDENT_in_page_pseudo599 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MARGIN_AREA_in_margin_rule610 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_margin_rule612 = new BitSet(new long[]{0x0040000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_LCURLY_in_margin_rule615 = new BitSet(new long[]{0x10900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_S_in_margin_rule617 = new BitSet(new long[]{0x10900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_declarations_in_margin_rule620 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_RCURLY_in_margin_rule622 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_margin_rule624 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_pseudo_in_inlineset647 = new BitSet(new long[]{0x0040000000020000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_inlineset649 = new BitSet(new long[]{0x0040000000020000L,0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_inlineset653 = new BitSet(new long[]{0x0000000000010000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_inlineset655 = new BitSet(new long[]{0x0000000000010000L,0x0000000000020000L});
	public static final BitSet FOLLOW_pseudo_in_inlineset658 = new BitSet(new long[]{0x0040000000020000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_inlineset660 = new BitSet(new long[]{0x0040000000020000L,0x0000000000020000L});
	public static final BitSet FOLLOW_LCURLY_in_inlineset673 = new BitSet(new long[]{0x10900228A0938040L,0x0000000002284944L});
	public static final BitSet FOLLOW_declarations_in_inlineset679 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_RCURLY_in_inlineset684 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_media_query_in_media711 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_media714 = new BitSet(new long[]{0x1188022CA4910040L,0x000000E0612289C4L});
	public static final BitSet FOLLOW_S_in_media716 = new BitSet(new long[]{0x1188022CA4910040L,0x000000E0612289C4L});
	public static final BitSet FOLLOW_media_query_in_media719 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_media_term_in_media_query753 = new BitSet(new long[]{0x1188022CA4910042L,0x000000E0612289C4L});
	public static final BitSet FOLLOW_S_in_media_query755 = new BitSet(new long[]{0x1188022CA4910042L,0x000000E0612289C4L});
	public static final BitSet FOLLOW_IDENT_in_media_term771 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_media_expression_in_media_term775 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nomediaquery_in_media_term781 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_media_expression803 = new BitSet(new long[]{0x0000002000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_media_expression805 = new BitSet(new long[]{0x0000002000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_IDENT_in_media_expression808 = new BitSet(new long[]{0x0000000000010000L,0x0000000000028000L});
	public static final BitSet FOLLOW_S_in_media_expression810 = new BitSet(new long[]{0x0000000000010000L,0x0000000000028000L});
	public static final BitSet FOLLOW_COLON_in_media_expression814 = new BitSet(new long[]{0x11E8023D24838140L,0x00000000612209C4L});
	public static final BitSet FOLLOW_S_in_media_expression816 = new BitSet(new long[]{0x11E8023D24838140L,0x00000000612209C4L});
	public static final BitSet FOLLOW_terms_in_media_expression819 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_RPAREN_in_media_expression823 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ruleset_in_media_rule855 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_atstatement_in_media_rule860 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_combined_selector_in_ruleset876 = new BitSet(new long[]{0x0040000000020000L});
	public static final BitSet FOLLOW_COMMA_in_ruleset879 = new BitSet(new long[]{0x0022003000018040L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_ruleset881 = new BitSet(new long[]{0x0022003000018040L,0x0000000000020000L});
	public static final BitSet FOLLOW_combined_selector_in_ruleset884 = new BitSet(new long[]{0x0040000000020000L});
	public static final BitSet FOLLOW_LCURLY_in_ruleset892 = new BitSet(new long[]{0x10900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_S_in_ruleset894 = new BitSet(new long[]{0x10900228A0938040L,0x00000000022A4944L});
	public static final BitSet FOLLOW_declarations_in_ruleset902 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_RCURLY_in_ruleset907 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_norule_in_ruleset926 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_declaration_in_declarations948 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
	public static final BitSet FOLLOW_SEMICOLON_in_declarations952 = new BitSet(new long[]{0x10900228A0938042L,0x00000000022A0944L});
	public static final BitSet FOLLOW_S_in_declarations954 = new BitSet(new long[]{0x10900228A0938042L,0x00000000022A0944L});
	public static final BitSet FOLLOW_declaration_in_declarations957 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
	public static final BitSet FOLLOW_property_in_declaration989 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_declaration991 = new BitSet(new long[]{0x11E8023DA4838142L,0x00000000612209C4L});
	public static final BitSet FOLLOW_S_in_declaration993 = new BitSet(new long[]{0x11E8023DA4838142L,0x00000000612209C4L});
	public static final BitSet FOLLOW_terms_in_declaration996 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_important_in_declaration999 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_noprop_in_declaration1019 = new BitSet(new long[]{0x11A8023CA4838042L,0x00000000612009C4L});
	public static final BitSet FOLLOW_any_in_declaration1021 = new BitSet(new long[]{0x11A8023CA4838042L,0x00000000612009C4L});
	public static final BitSet FOLLOW_EXCLAMATION_in_important1047 = new BitSet(new long[]{0x0000000000000000L,0x0000010000020000L});
	public static final BitSet FOLLOW_S_in_important1049 = new BitSet(new long[]{0x0000000000000000L,0x0000010000020000L});
	public static final BitSet FOLLOW_104_in_important1052 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_important1054 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_MINUS_in_property1083 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_IDENT_in_property1086 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_property1088 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_term_in_terms1116 = new BitSet(new long[]{0x11E8023D24838142L,0x00000000612009C4L});
	public static final BitSet FOLLOW_valuepart_in_term1149 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LCURLY_in_term1161 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612A49C4L});
	public static final BitSet FOLLOW_S_in_term1163 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612A49C4L});
	public static final BitSet FOLLOW_any_in_term1167 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612849C4L});
	public static final BitSet FOLLOW_SEMICOLON_in_term1171 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612A49C4L});
	public static final BitSet FOLLOW_S_in_term1173 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612A49C4L});
	public static final BitSet FOLLOW_RCURLY_in_term1178 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ATKEYWORD_in_term1190 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_term1192 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_EXPRESSION_in_funct1225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNCTION_in_funct1234 = new BitSet(new long[]{0x11E8023D24838140L,0x00000000612289C4L});
	public static final BitSet FOLLOW_S_in_funct1236 = new BitSet(new long[]{0x11E8023D24838140L,0x00000000612289C4L});
	public static final BitSet FOLLOW_terms_in_funct1239 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_RPAREN_in_funct1242 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1269 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_IDENT_in_valuepart1272 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_CLASSKEYWORD_in_valuepart1289 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1303 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_NUMBER_in_valuepart1306 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1323 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_PERCENTAGE_in_valuepart1326 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1343 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_DIMENSION_in_valuepart1346 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_string_in_valuepart1363 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_URI_in_valuepart1377 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_HASH_in_valuepart1394 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_UNIRANGE_in_valuepart1408 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_INCLUDES_in_valuepart1422 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_COLON_in_valuepart1436 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_valuepart1450 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_GREATER_in_valuepart1464 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_LESS_in_valuepart1478 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_QUESTION_in_valuepart1492 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_PERCENT_in_valuepart1506 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_EQUALS_in_valuepart1520 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_SLASH_in_valuepart1534 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_PLUS_in_valuepart1547 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_ASTERISK_in_valuepart1560 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1577 = new BitSet(new long[]{0x0000000500000000L});
	public static final BitSet FOLLOW_funct_in_valuepart1580 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_DASHMATCH_in_valuepart1598 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_LPAREN_in_valuepart1612 = new BitSet(new long[]{0x11A8023D24838040L,0x00000000612089C4L});
	public static final BitSet FOLLOW_valuepart_in_valuepart1614 = new BitSet(new long[]{0x11A8023D24838040L,0x00000000612089C4L});
	public static final BitSet FOLLOW_RPAREN_in_valuepart1617 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_LBRACE_in_valuepart1636 = new BitSet(new long[]{0x11A8023D24838040L,0x00000000612029C4L});
	public static final BitSet FOLLOW_valuepart_in_valuepart1638 = new BitSet(new long[]{0x11A8023D24838040L,0x00000000612029C4L});
	public static final BitSet FOLLOW_RBRACE_in_valuepart1641 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_valuepart1659 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_selector_in_combined_selector1676 = new BitSet(new long[]{0x0000000800000002L,0x0000000010020100L});
	public static final BitSet FOLLOW_combinator_in_combined_selector1680 = new BitSet(new long[]{0x0022003000018040L});
	public static final BitSet FOLLOW_selector_in_combined_selector1683 = new BitSet(new long[]{0x0000000800000002L,0x0000000010020100L});
	public static final BitSet FOLLOW_GREATER_in_combinator1703 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_combinator1705 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_PLUS_in_combinator1715 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_combinator1717 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_TILDE_in_combinator1727 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_combinator1729 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_combinator1739 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENT_in_selector1758 = new BitSet(new long[]{0x0022001000018002L,0x0000000000020000L});
	public static final BitSet FOLLOW_ASTERISK_in_selector1762 = new BitSet(new long[]{0x0022001000018002L,0x0000000000020000L});
	public static final BitSet FOLLOW_selpart_in_selector1766 = new BitSet(new long[]{0x0022001000018002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_selector1769 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_selpart_in_selector1799 = new BitSet(new long[]{0x0022001000018002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_selector1802 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_HASH_in_selpart1849 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CLASSKEYWORD_in_selpart1857 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACE_in_selpart1864 = new BitSet(new long[]{0x0000002000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_selpart1866 = new BitSet(new long[]{0x0000002000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_attribute_in_selpart1869 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_RBRACE_in_selpart1871 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pseudo_in_selpart1887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INVALID_SELPART_in_selpart1895 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENT_in_attribute1919 = new BitSet(new long[]{0x0000020030880002L,0x0000000000820000L});
	public static final BitSet FOLLOW_S_in_attribute1921 = new BitSet(new long[]{0x0000020030880002L,0x0000000000820000L});
	public static final BitSet FOLLOW_set_in_attribute1929 = new BitSet(new long[]{0x0008002000000000L,0x0000000001020000L});
	public static final BitSet FOLLOW_S_in_attribute1953 = new BitSet(new long[]{0x0008002000000000L,0x0000000001020000L});
	public static final BitSet FOLLOW_IDENT_in_attribute1958 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_string_in_attribute1962 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_attribute1965 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_pseudocolon_in_pseudo1980 = new BitSet(new long[]{0x0000002400000000L});
	public static final BitSet FOLLOW_IDENT_in_pseudo1984 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNCTION_in_pseudo1988 = new BitSet(new long[]{0x1000042000000000L,0x0000000000020004L});
	public static final BitSet FOLLOW_S_in_pseudo1990 = new BitSet(new long[]{0x1000042000000000L,0x0000000000020004L});
	public static final BitSet FOLLOW_IDENT_in_pseudo1995 = new BitSet(new long[]{0x0000000000000000L,0x0000000000028000L});
	public static final BitSet FOLLOW_MINUS_in_pseudo1999 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_NUMBER_in_pseudo2002 = new BitSet(new long[]{0x0000000000000000L,0x0000000000028000L});
	public static final BitSet FOLLOW_MINUS_in_pseudo2006 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_INDEX_in_pseudo2009 = new BitSet(new long[]{0x0000000000000000L,0x0000000000028000L});
	public static final BitSet FOLLOW_S_in_pseudo2012 = new BitSet(new long[]{0x0000000000000000L,0x0000000000028000L});
	public static final BitSet FOLLOW_RPAREN_in_pseudo2016 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLON_in_pseudocolon2037 = new BitSet(new long[]{0x0000000000010002L});
	public static final BitSet FOLLOW_COLON_in_pseudocolon2039 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENT_in_any2076 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_CLASSKEYWORD_in_any2087 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_NUMBER_in_any2098 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_PERCENTAGE_in_any2109 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_DIMENSION_in_any2119 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_string_in_any2130 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_URI_in_any2144 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_HASH_in_any2161 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_UNIRANGE_in_any2175 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_INCLUDES_in_any2189 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_COLON_in_any2203 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_any2217 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_GREATER_in_any2231 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_LESS_in_any2245 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_QUESTION_in_any2259 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_PERCENT_in_any2273 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_EQUALS_in_any2287 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_SLASH_in_any2301 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_EXCLAMATION_in_any2315 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_MINUS_in_any2326 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_PLUS_in_any2337 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_ASTERISK_in_any2348 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_FUNCTION_in_any2365 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612289C4L});
	public static final BitSet FOLLOW_S_in_any2367 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612289C4L});
	public static final BitSet FOLLOW_any_in_any2370 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612089C4L});
	public static final BitSet FOLLOW_RPAREN_in_any2373 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_DASHMATCH_in_any2393 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_LPAREN_in_any2407 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612089C4L});
	public static final BitSet FOLLOW_any_in_any2409 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612089C4L});
	public static final BitSet FOLLOW_RPAREN_in_any2412 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_LBRACE_in_any2431 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612029C4L});
	public static final BitSet FOLLOW_any_in_any2433 = new BitSet(new long[]{0x11A8023CA4838040L,0x00000000612029C4L});
	public static final BitSet FOLLOW_RBRACE_in_any2436 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_any2454 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_RCURLY_in_nostatement2469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SEMICOLON_in_nostatement2483 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QUOT_in_nostatement2497 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_APOS_in_nostatement2511 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CLASSKEYWORD_in_noprop2534 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_NUMBER_in_noprop2547 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_noprop2559 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_GREATER_in_noprop2571 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_LESS_in_noprop2583 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_QUESTION_in_noprop2595 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_PERCENT_in_noprop2607 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_EQUALS_in_noprop2619 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_SLASH_in_noprop2631 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_EXCLAMATION_in_noprop2643 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_PLUS_in_noprop2655 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_ASTERISK_in_noprop2667 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_DASHMATCH_in_noprop2682 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_INCLUDES_in_noprop2694 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_COLON_in_noprop2706 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_STRING_CHAR_in_noprop2718 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_CTRL_in_noprop2731 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_INVALID_TOKEN_in_noprop2743 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_S_in_noprop2756 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
	public static final BitSet FOLLOW_NUMBER_in_norule2771 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PERCENTAGE_in_norule2784 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DIMENSION_in_norule2796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_in_norule2809 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_URI_in_norule2823 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_UNIRANGE_in_norule2840 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INCLUDES_in_norule2854 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COMMA_in_norule2868 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GREATER_in_norule2882 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LESS_in_norule2896 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QUESTION_in_norule2910 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PERCENT_in_norule2924 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EQUALS_in_norule2938 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SLASH_in_norule2952 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EXCLAMATION_in_norule2966 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_norule2979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PLUS_in_norule2992 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DASHMATCH_in_norule3006 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RPAREN_in_norule3020 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CTRL_in_norule3034 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_norule3048 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_norule3059 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_norule3069 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NUMBER_in_nomediaquery3093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PERCENTAGE_in_nomediaquery3107 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DIMENSION_in_nomediaquery3120 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_in_nomediaquery3134 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_URI_in_nomediaquery3148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_UNIRANGE_in_nomediaquery3165 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INCLUDES_in_nomediaquery3179 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GREATER_in_nomediaquery3193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LESS_in_nomediaquery3207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QUESTION_in_nomediaquery3221 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PERCENT_in_nomediaquery3235 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EQUALS_in_nomediaquery3249 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SLASH_in_nomediaquery3263 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EXCLAMATION_in_nomediaquery3277 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_nomediaquery3291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PLUS_in_nomediaquery3305 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DASHMATCH_in_nomediaquery3319 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RPAREN_in_nomediaquery3333 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CTRL_in_nomediaquery3347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLON_in_nomediaquery3361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ASTERISK_in_nomediaquery3375 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNCTION_in_nomediaquery3389 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_nomediaquery3403 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_nomediaquery3414 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_nomediaquery3424 = new BitSet(new long[]{0x0000000000000002L});
}
