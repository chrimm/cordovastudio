// $ANTLR 3.5.2 cz/vutbr/web/csskit/antlr/CSSTreeParser.g 2014-07-11 12:43:54

package cz.vutbr.web.csskit.antlr;

import cz.vutbr.web.css.*;
import cz.vutbr.web.csskit.PriorityStrategy;
import cz.vutbr.web.csskit.RuleArrayList;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// @SuppressWarnings("unchecked")

@SuppressWarnings("all")
public class CSSTreeParser extends TreeParser {
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
	public TreeParser[] getDelegates() {
		return new TreeParser[] {};
	}

	// delegators


	public CSSTreeParser(TreeNodeStream input) {
		this(input, new RecognizerSharedState());
	}
	public CSSTreeParser(TreeNodeStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return CSSTreeParser.tokenNames; }
	@Override public String getGrammarFileName() { return "cz/vutbr/web/csskit/antlr/CSSTreeParser.g"; }


		private static Logger log = LoggerFactory.getLogger(CSSTreeParser.class);

		private static RuleFactory rf = CSSFactory.getRuleFactory();
		private static TermFactory tf = CSSFactory.getTermFactory();
		private static SupportedCSS css = CSSFactory.getSupportedCSS();

		private enum MediaQueryState { START, TYPE, AND, EXPR, TYPEOREXPR }

	    // block preparator
		private Preparator preparator;
		private List<MediaQuery> wrapMedia;
		private RuleList rules;
		private List<List<MediaQuery>> importMedia;
		private List<String> importPaths;
		
		//prevent imports inside the style sheet
		private boolean preventImports;
		

	  /**
	   * Initializes the tree parser.
	   * @param preparator The preparator to be used for creating the rules.
	   * @param wrapMedia The media queries to be used for wrapping the created rules (e.g. in case
	   *    of parsing and imported style sheet) or null when no wrapping is required.
	   * @return The initialized tree parser 
	   */
	  public CSSTreeParser init(Preparator preparator, List<MediaQuery> wrapMedia) {
			this.preparator = preparator;
			this.wrapMedia = wrapMedia;
			this.rules = null;
			this.importMedia = new ArrayList<List<MediaQuery>>();
			this.importPaths = new ArrayList<String>();
			this.preventImports = false;
			return this;
		}   
	  
	  public StyleSheet addRulesToStyleSheet(StyleSheet sheet, PriorityStrategy ps)
	  {
	    if (rules != null)
	    {
	      for (RuleBlock<?> rule : rules)
	      {
	          rule.setPriority(ps.getAndIncrement());
	          if (rule instanceof RuleMedia) //@media: assign priority to contained rules
	          {
	            if (rule instanceof RuleMedia)
	            {
	                for (RuleSet inrule : (RuleMedia) rule)
	                    inrule.setPriority(ps.getAndIncrement());
	            }
	          }
	          sheet.add(rule); 
	      }
	      sheet.markLast(ps.markAndIncrement());
	    }
	    return sheet;
	  }
	  
	  public RuleList getRules()
	  {
	    return rules;
	  }
	  
	  public List<List<MediaQuery>> getImportMedia()
	  {
	    return importMedia;
	  } 
	  
	  public List<String> getImportPaths()
	  {
	    return importPaths;
	  }
	  
	  @Override
		public void emitErrorMessage(String msg) {
		    log.info("ANTLR: {}", msg);
		}
			
		private String extractText(CommonTree token) {
	        return token.getText();
	    }
	   
	  private URL extractBase(CommonTree token) {
	      CSSToken ct = (CSSToken) token.getToken();
	      return ct.getBase();
	  }
	    	
	  private Declaration.Source extractSource(CommonTree token) {
	      CSSToken ct = (CSSToken) token.getToken();
	      Declaration.Source src = new Declaration.Source(ct.getBase(), ct.getLine(), ct.getCharPositionInLine());
	      return src;
	  }   
			
	    private void logEnter(String entry) {
	        log.trace("Entering '{}'", entry);
	    }
	    	
	    private void logLeave(String leaving) {
		    log.trace("Leaving '{}'", leaving);
	    }



	// $ANTLR start "inlinestyle"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:150:1: inlinestyle returns [RuleList rules] : ( ^( INLINESTYLE decl= declarations ) | ^( INLINESTYLE (irs= inlineset )+ ) );
	public final RuleList inlinestyle() throws RecognitionException {
		RuleList rules = null;


		List<Declaration> decl =null;
		RuleBlock<?> irs =null;


			logEnter("inlinestyle");
			rules = this.rules = new RuleArrayList();

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:159:2: ( ^( INLINESTYLE decl= declarations ) | ^( INLINESTYLE (irs= inlineset )+ ) )
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==INLINESTYLE) ) {
				int LA2_1 = input.LA(2);
				if ( (LA2_1==DOWN) ) {
					int LA2_2 = input.LA(3);
					if ( (LA2_2==SET) ) {
						alt2=1;
					}
					else if ( (LA2_2==RULE) ) {
						alt2=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 2, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 2, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}

			switch (alt2) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:159:5: ^( INLINESTYLE decl= declarations )
					{
					match(input,INLINESTYLE,FOLLOW_INLINESTYLE_in_inlinestyle59); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_declarations_in_inlinestyle63);
					decl=declarations();
					state._fsp--;

					match(input, Token.UP, null); 


								RuleBlock<?> rb = preparator.prepareInlineRuleSet(decl, null);
								if(rb!=null) {
								     rules.add(rb);
								}
							
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:166:6: ^( INLINESTYLE (irs= inlineset )+ )
					{
					match(input,INLINESTYLE,FOLLOW_INLINESTYLE_in_inlinestyle78); 
					match(input, Token.DOWN, null); 
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:167:5: (irs= inlineset )+
					int cnt1=0;
					loop1:
					while (true) {
						int alt1=2;
						int LA1_0 = input.LA(1);
						if ( (LA1_0==RULE) ) {
							alt1=1;
						}

						switch (alt1) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:167:6: irs= inlineset
							{
							pushFollow(FOLLOW_inlineset_in_inlinestyle88);
							irs=inlineset();
							state._fsp--;

							if(irs!=null) rules.add(irs);
							}
							break;

						default :
							if ( cnt1 >= 1 ) break loop1;
							EarlyExitException eee = new EarlyExitException(1, input);
							throw eee;
						}
						cnt1++;
					}

					match(input, Token.UP, null); 

					}
					break;

			}

				log.debug("\n***\n{}\n***\n", rules);	   
				logLeave("inlinestyle");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return rules;
	}
	// $ANTLR end "inlinestyle"



	// $ANTLR start "stylesheet"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:174:1: stylesheet returns [RuleList rules] : ^( STYLESHEET (s= statement )* ) ;
	public final RuleList stylesheet() throws RecognitionException {
		RuleList rules = null;


		RuleBlock<?> s =null;


			logEnter("stylesheet");
		  rules = this.rules = new RuleArrayList();

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:183:2: ( ^( STYLESHEET (s= statement )* ) )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:183:4: ^( STYLESHEET (s= statement )* )
			{
			match(input,STYLESHEET,FOLLOW_STYLESHEET_in_stylesheet125); 
			if ( input.LA(1)==Token.DOWN ) {
				match(input, Token.DOWN, null); 
				// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:184:4: (s= statement )*
				loop3:
				while (true) {
					int alt3=2;
					int LA3_0 = input.LA(1);
					if ( (LA3_0==CHARSET||LA3_0==FONTFACE||LA3_0==IMPORT||LA3_0==INVALID_IMPORT||LA3_0==INVALID_STATEMENT||LA3_0==MEDIA||LA3_0==PAGE||LA3_0==RULE||LA3_0==VIEWPORT) ) {
						alt3=1;
					}

					switch (alt3) {
					case 1 :
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:184:5: s= statement
						{
						pushFollow(FOLLOW_statement_in_stylesheet134);
						s=statement();
						state._fsp--;

						 if(s!=null) rules.add(s);
						}
						break;

					default :
						break loop3;
					}
				}

				match(input, Token.UP, null); 
			}

			}


				log.debug("\n***\n{}\n***\n", rules);
				logLeave("stylesheet");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return rules;
	}
	// $ANTLR end "stylesheet"


	protected static class statement_scope {
		boolean invalid;
		boolean insideAtstatement;
	}
	protected Stack<statement_scope> statement_stack = new Stack<statement_scope>();


	// $ANTLR start "statement"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:191:1: statement returns [RuleBlock<?> stm] : (rs= ruleset |ats= atstatement | INVALID_STATEMENT );
	public final RuleBlock<?> statement() throws RecognitionException {
		statement_stack.push(new statement_scope());
		RuleBlock<?> stm = null;


		RuleBlock<?> rs =null;
		RuleBlock<?> ats =null;


			logEnter("statement");
			statement_stack.peek().invalid = false;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:209:2: (rs= ruleset |ats= atstatement | INVALID_STATEMENT )
			int alt4=3;
			switch ( input.LA(1) ) {
			case RULE:
				{
				alt4=1;
				}
				break;
			case CHARSET:
			case FONTFACE:
			case IMPORT:
			case INVALID_IMPORT:
			case MEDIA:
			case PAGE:
			case VIEWPORT:
				{
				alt4=2;
				}
				break;
			case INVALID_STATEMENT:
				{
				alt4=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}
			switch (alt4) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:209:4: rs= ruleset
					{
					pushFollow(FOLLOW_ruleset_in_statement183);
					rs=ruleset();
					state._fsp--;

					stm =(RuleBlock<?>) rs;
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:210:4: ats= atstatement
					{
					pushFollow(FOLLOW_atstatement_in_statement193);
					ats=atstatement();
					state._fsp--;

					stm =(RuleBlock<?>) ats;
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:211:4: INVALID_STATEMENT
					{
					match(input,INVALID_STATEMENT,FOLLOW_INVALID_STATEMENT_in_statement200); 
					 statement_stack.peek().invalid = true; 
					}
					break;

			}

			  if (statement_stack.peek().invalid)
			      log.debug("Statement is invalid");
				logLeave("statement");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
			statement_stack.pop();
		}
		return stm;
	}
	// $ANTLR end "statement"


	protected static class atstatement_scope {
		RuleBlock<?> stm;
	}
	protected Stack<atstatement_scope> atstatement_stack = new Stack<atstatement_scope>();


	// $ANTLR start "atstatement"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:215:1: atstatement returns [RuleBlock<?> stmnt] : ( CHARSET | INVALID_IMPORT | ^( IMPORT (im= media )? (iuri= import_uri ) ) | ^( PAGE (i= IDENT )? ( ^( PSEUDO i= IDENT ) )? decl= declarations ^( SET (m= margin )* ) ) | ^( VIEWPORT decl= declarations ) | ^( FONTFACE decl= declarations ) | ^( MEDIA (mediaList= media )? (rs= ruleset | INVALID_STATEMENT )* ) );
	public final RuleBlock<?> atstatement() throws RecognitionException {
		atstatement_stack.push(new atstatement_scope());
		RuleBlock<?> stmnt = null;


		CommonTree i=null;
		List<MediaQuery> im =null;
		String iuri =null;
		List<Declaration> decl =null;
		RuleMargin m =null;
		List<MediaQuery> mediaList =null;
		RuleBlock<?> rs =null;


		    logEnter("atstatement");
			statement_stack.peek().insideAtstatement =true;
			atstatement_stack.peek().stm = stmnt = null;
			List<RuleSet> rules = null;
			List<RuleMargin> margins = null;
			String name = null;
			String pseudo = null;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:231:2: ( CHARSET | INVALID_IMPORT | ^( IMPORT (im= media )? (iuri= import_uri ) ) | ^( PAGE (i= IDENT )? ( ^( PSEUDO i= IDENT ) )? decl= declarations ^( SET (m= margin )* ) ) | ^( VIEWPORT decl= declarations ) | ^( FONTFACE decl= declarations ) | ^( MEDIA (mediaList= media )? (rs= ruleset | INVALID_STATEMENT )* ) )
			int alt11=7;
			switch ( input.LA(1) ) {
			case CHARSET:
				{
				alt11=1;
				}
				break;
			case INVALID_IMPORT:
				{
				alt11=2;
				}
				break;
			case IMPORT:
				{
				alt11=3;
				}
				break;
			case PAGE:
				{
				alt11=4;
				}
				break;
			case VIEWPORT:
				{
				alt11=5;
				}
				break;
			case FONTFACE:
				{
				alt11=6;
				}
				break;
			case MEDIA:
				{
				alt11=7;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 11, 0, input);
				throw nvae;
			}
			switch (alt11) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:231:4: CHARSET
					{
					match(input,CHARSET,FOLLOW_CHARSET_in_atstatement233); 
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:232:4: INVALID_IMPORT
					{
					match(input,INVALID_IMPORT,FOLLOW_INVALID_IMPORT_in_atstatement239); 
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:233:4: ^( IMPORT (im= media )? (iuri= import_uri ) )
					{
					match(input,IMPORT,FOLLOW_IMPORT_in_atstatement246); 
					match(input, Token.DOWN, null); 
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:234:8: (im= media )?
					int alt5=2;
					int LA5_0 = input.LA(1);
					if ( (LA5_0==MEDIA_QUERY) ) {
						alt5=1;
					}
					switch (alt5) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:234:9: im= media
							{
							pushFollow(FOLLOW_media_in_atstatement258);
							im=media();
							state._fsp--;

							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:235:8: (iuri= import_uri )
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:235:9: iuri= import_uri
					{
					pushFollow(FOLLOW_import_uri_in_atstatement272);
					iuri=import_uri();
					state._fsp--;

					}

					match(input, Token.UP, null); 


						    if (!this.preventImports)
						    {
							    log.debug("Adding import: {}", iuri);
							    importMedia.add(im);
							    importPaths.add(iuri);
							  }
							  else 
					        log.debug("Ignoring import: {}", iuri);
						  
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:247:5: ^( PAGE (i= IDENT )? ( ^( PSEUDO i= IDENT ) )? decl= declarations ^( SET (m= margin )* ) )
					{
					match(input,PAGE,FOLLOW_PAGE_in_atstatement291); 
					match(input, Token.DOWN, null); 
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:248:7: (i= IDENT )?
					int alt6=2;
					int LA6_0 = input.LA(1);
					if ( (LA6_0==IDENT) ) {
						alt6=1;
					}
					switch (alt6) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:248:8: i= IDENT
							{
							i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_atstatement302); 
							 name = extractText(i); 
							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:251:7: ( ^( PSEUDO i= IDENT ) )?
					int alt7=2;
					int LA7_0 = input.LA(1);
					if ( (LA7_0==PSEUDO) ) {
						alt7=1;
					}
					switch (alt7) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:251:8: ^( PSEUDO i= IDENT )
							{
							match(input,PSEUDO,FOLLOW_PSEUDO_in_atstatement331); 
							match(input, Token.DOWN, null); 
							i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_atstatement335); 
							match(input, Token.UP, null); 

							 pseudo = extractText(i); 
							}
							break;

					}

					pushFollow(FOLLOW_declarations_in_atstatement365);
					decl=declarations();
					state._fsp--;

					match(input,SET,FOLLOW_SET_in_atstatement374); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:255:13: (m= margin )*
						loop8:
						while (true) {
							int alt8=2;
							int LA8_0 = input.LA(1);
							if ( (LA8_0==MARGIN_AREA) ) {
								alt8=1;
							}

							switch (alt8) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:255:14: m= margin
								{
								pushFollow(FOLLOW_margin_in_atstatement379);
								m=margin();
								state._fsp--;


								        if (m!=null) {
								          if (margins == null) margins = new ArrayList<RuleMargin>();
								          margins.add(m);
								          log.debug("Inserted margin rule #{} into @page", margins.size()+1);
								        }
								      
								}
								break;

							default :
								break loop8;
							}
						}

						match(input, Token.UP, null); 
					}

					match(input, Token.UP, null); 


					      stmnt = preparator.prepareRulePage(decl, margins, name, pseudo);
					      this.preventImports = true;
					    
					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:267:5: ^( VIEWPORT decl= declarations )
					{
					match(input,VIEWPORT,FOLLOW_VIEWPORT_in_atstatement403); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_declarations_in_atstatement407);
					decl=declarations();
					state._fsp--;

					match(input, Token.UP, null); 

					 stmnt = preparator.prepareRuleViewport(decl); this.preventImports = true; 
					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:269:5: ^( FONTFACE decl= declarations )
					{
					match(input,FONTFACE,FOLLOW_FONTFACE_in_atstatement421); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_declarations_in_atstatement425);
					decl=declarations();
					state._fsp--;

					match(input, Token.UP, null); 

					 stmnt = preparator.prepareRuleFontFace(decl); this.preventImports = true; 
					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:271:4: ^( MEDIA (mediaList= media )? (rs= ruleset | INVALID_STATEMENT )* )
					{
					match(input,MEDIA,FOLLOW_MEDIA_in_atstatement438); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:271:12: (mediaList= media )?
						int alt9=2;
						int LA9_0 = input.LA(1);
						if ( (LA9_0==MEDIA_QUERY) ) {
							alt9=1;
						}
						switch (alt9) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:271:13: mediaList= media
								{
								pushFollow(FOLLOW_media_in_atstatement443);
								mediaList=media();
								state._fsp--;

								}
								break;

						}

						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:272:4: (rs= ruleset | INVALID_STATEMENT )*
						loop10:
						while (true) {
							int alt10=3;
							int LA10_0 = input.LA(1);
							if ( (LA10_0==RULE) ) {
								alt10=1;
							}
							else if ( (LA10_0==INVALID_STATEMENT) ) {
								alt10=2;
							}

							switch (alt10) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:272:7: rs= ruleset
								{
								pushFollow(FOLLOW_ruleset_in_atstatement456);
								rs=ruleset();
								state._fsp--;


													   if(rules==null) rules = new ArrayList<RuleSet>();				
													   if(rs!=null) {
														   // this cast should be safe, because when inside of @statetement, oridinal ruleset
														   // is returned
													       rules.add((RuleSet)rs);
														   log.debug("Inserted ruleset ({}) into @media", rules.size());
													   }
													
								}
								break;
							case 2 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:281:8: INVALID_STATEMENT
								{
								match(input,INVALID_STATEMENT,FOLLOW_INVALID_STATEMENT_in_atstatement467); 
								 log.debug("Skiping invalid statement in media"); 
								}
								break;

							default :
								break loop10;
							}
						}

						match(input, Token.UP, null); 
					}


							   stmnt = preparator.prepareRuleMedia(rules, mediaList);
							   this.preventImports = true;
						   
					}
					break;

			}

			    logLeave("atstatement");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
			atstatement_stack.pop();
		}
		return stmnt;
	}
	// $ANTLR end "atstatement"



	// $ANTLR start "import_uri"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:291:1: import_uri returns [String s] : ( (uri= URI ) | (str= STRING ) );
	public final String import_uri() throws RecognitionException {
		String s = null;


		CommonTree uri=null;
		CommonTree str=null;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:292:3: ( (uri= URI ) | (str= STRING ) )
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==URI) ) {
				alt12=1;
			}
			else if ( (LA12_0==STRING) ) {
				alt12=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 12, 0, input);
				throw nvae;
			}

			switch (alt12) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:292:5: (uri= URI )
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:292:5: (uri= URI )
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:292:6: uri= URI
					{
					uri=(CommonTree)match(input,URI,FOLLOW_URI_in_import_uri511); 
					}

					 s = extractText(uri); 
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:293:5: (str= STRING )
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:293:5: (str= STRING )
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:293:6: str= STRING
					{
					str=(CommonTree)match(input,STRING,FOLLOW_STRING_in_import_uri523); 
					}

					 s = extractText(str); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return s;
	}
	// $ANTLR end "import_uri"



	// $ANTLR start "margin"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:296:1: margin returns [RuleMargin m] : ^(area= MARGIN_AREA decl= declarations ) ;
	public final RuleMargin margin() throws RecognitionException {
		RuleMargin m = null;


		CommonTree area=null;
		List<Declaration> decl =null;


		    logEnter("margin");

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:303:2: ( ^(area= MARGIN_AREA decl= declarations ) )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:303:4: ^(area= MARGIN_AREA decl= declarations )
			{
			area=(CommonTree)match(input,MARGIN_AREA,FOLLOW_MARGIN_AREA_in_margin557); 
			match(input, Token.DOWN, null); 
			pushFollow(FOLLOW_declarations_in_margin563);
			decl=declarations();
			state._fsp--;

			match(input, Token.UP, null); 

			 m = preparator.prepareRuleMargin(extractText(area).substring(1), decl); 
			}


			    logLeave("margin");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return m;
	}
	// $ANTLR end "margin"



	// $ANTLR start "media"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:308:1: media returns [List<MediaQuery> queries] : (q= mediaquery )+ ;
	public final List<MediaQuery> media() throws RecognitionException {
		List<MediaQuery> queries = null;


		MediaQuery q =null;


		   logEnter("media");
		   queries = new ArrayList<MediaQuery>();

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:317:2: ( (q= mediaquery )+ )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:317:4: (q= mediaquery )+
			{
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:317:4: (q= mediaquery )+
			int cnt13=0;
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( (LA13_0==MEDIA_QUERY) ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:317:5: q= mediaquery
					{
					pushFollow(FOLLOW_mediaquery_in_media599);
					q=mediaquery();
					state._fsp--;


									   queries.add(q);
					    
					}
					break;

				default :
					if ( cnt13 >= 1 ) break loop13;
					EarlyExitException eee = new EarlyExitException(13, input);
					throw eee;
				}
				cnt13++;
			}

			}


			   log.debug("Totally returned {} media queries.", queries.size());							  
			   logLeave("media");		   

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return queries;
	}
	// $ANTLR end "media"


	protected static class mediaquery_scope {
		MediaQuery q;
		MediaQueryState state;
		boolean invalid;
	}
	protected Stack<mediaquery_scope> mediaquery_stack = new Stack<mediaquery_scope>();


	// $ANTLR start "mediaquery"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:322:1: mediaquery returns [MediaQuery query] : ^( MEDIA_QUERY ( mediaterm )+ ) ;
	public final MediaQuery mediaquery() throws RecognitionException {
		mediaquery_stack.push(new mediaquery_scope());
		MediaQuery query = null;



		    logEnter("mediaquery");
		    mediaquery_stack.peek().q = query = rf.createMediaQuery();
		    query.unlock();
		    mediaquery_stack.peek().state = MediaQueryState.START;
		    mediaquery_stack.peek().invalid = false;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:344:3: ( ^( MEDIA_QUERY ( mediaterm )+ ) )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:344:5: ^( MEDIA_QUERY ( mediaterm )+ )
			{
			match(input,MEDIA_QUERY,FOLLOW_MEDIA_QUERY_in_mediaquery635); 
			match(input, Token.DOWN, null); 
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:344:19: ( mediaterm )+
			int cnt14=0;
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==DECLARATION||LA14_0==IDENT||LA14_0==INVALID_DECLARATION||LA14_0==INVALID_STATEMENT) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:344:19: mediaterm
					{
					pushFollow(FOLLOW_mediaterm_in_mediaquery637);
					mediaterm();
					state._fsp--;

					}
					break;

				default :
					if ( cnt14 >= 1 ) break loop14;
					EarlyExitException eee = new EarlyExitException(14, input);
					throw eee;
				}
				cnt14++;
			}

			match(input, Token.UP, null); 

			}


			    if (mediaquery_stack.peek().invalid)
			    {
			        log.trace("Skipping invalid rule {}", query);
			        mediaquery_stack.peek().q.setType("all"); //change the malformed media queries to "not all"
			        mediaquery_stack.peek().q.setNegative(true);
			    }
			    logLeave("mediaquery");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
			mediaquery_stack.pop();
		}
		return query;
	}
	// $ANTLR end "mediaquery"



	// $ANTLR start "mediaterm"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:347:1: mediaterm : ( (i= IDENT ) | (e= mediaexpression ) | ( INVALID_STATEMENT ) );
	public final void mediaterm() throws RecognitionException {
		CommonTree i=null;
		MediaExpression e =null;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:348:3: ( (i= IDENT ) | (e= mediaexpression ) | ( INVALID_STATEMENT ) )
			int alt15=3;
			switch ( input.LA(1) ) {
			case IDENT:
				{
				alt15=1;
				}
				break;
			case DECLARATION:
			case INVALID_DECLARATION:
				{
				alt15=2;
				}
				break;
			case INVALID_STATEMENT:
				{
				alt15=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}
			switch (alt15) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:348:5: (i= IDENT )
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:348:5: (i= IDENT )
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:348:6: i= IDENT
					{
					i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_mediaterm655); 

					            String m = extractText(i);
					            MediaQueryState state = mediaquery_stack.peek().state;
					            if (m.equalsIgnoreCase("ONLY") && state == MediaQueryState.START)
					            {
					                mediaquery_stack.peek().state = MediaQueryState.TYPEOREXPR;
					            }
					            else if (m.equalsIgnoreCase("NOT") && state == MediaQueryState.START)
					            {
					                mediaquery_stack.peek().q.setNegative(true);
					                mediaquery_stack.peek().state = MediaQueryState.TYPEOREXPR;
					            }
					            else if (m.equalsIgnoreCase("AND") && state == MediaQueryState.AND)
					            {
					                mediaquery_stack.peek().state = MediaQueryState.EXPR;
					            }
					            else if (state == MediaQueryState.START
					                      || state == MediaQueryState.TYPE
					                      || state == MediaQueryState.TYPEOREXPR)
					            { 
					                mediaquery_stack.peek().q.setType(m);
					                mediaquery_stack.peek().state = MediaQueryState.AND;
					            }
					            else
					            {
					                log.trace("Invalid media query: found ident: {} state: {}", m, state);
					                mediaquery_stack.peek().invalid = true;
					            }
					        
					}

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:378:6: (e= mediaexpression )
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:378:6: (e= mediaexpression )
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:378:7: e= mediaexpression
					{
					pushFollow(FOLLOW_mediaexpression_in_mediaterm675);
					e=mediaexpression();
					state._fsp--;


					            if (mediaquery_stack.peek().state == MediaQueryState.START 
					                || mediaquery_stack.peek().state == MediaQueryState.EXPR
					                || mediaquery_stack.peek().state == MediaQueryState.TYPEOREXPR)
					            {
					                if (e.getFeature() != null) //the expression is valid
					                {
							                mediaquery_stack.peek().q.add(e); 
							                mediaquery_stack.peek().state = MediaQueryState.AND;
							            }
							            else
							            {
							                log.trace("Invalidating media query for invalud expression");
							                mediaquery_stack.peek().invalid = true;
							            }
					            }
					            else
					            {
					                log.trace("Invalid media query: found expr, state: {}", mediaquery_stack.peek().state);
					                mediaquery_stack.peek().invalid = true;
					            }
					      
					}

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:400:6: ( INVALID_STATEMENT )
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:400:6: ( INVALID_STATEMENT )
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:400:7: INVALID_STATEMENT
					{
					match(input,INVALID_STATEMENT,FOLLOW_INVALID_STATEMENT_in_mediaterm686); 

					            mediaquery_stack.peek().invalid = true;
					      
					}

					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "mediaterm"



	// $ANTLR start "mediaexpression"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:405:1: mediaexpression returns [MediaExpression expr] : d= declaration ;
	public final MediaExpression mediaexpression() throws RecognitionException {
		MediaExpression expr = null;


		Declaration d =null;


		    logEnter("mediaquery");
		    expr = rf.createMediaExpression();

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:413:5: (d= declaration )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:413:7: d= declaration
			{
			pushFollow(FOLLOW_declaration_in_mediaexpression721);
			d=declaration();
			state._fsp--;

			 
			          if (d != null) { //if the declaration is valid
			              expr.setFeature(d.getProperty()); 
			              expr.replaceAll(d);
			          } 
			      
			}


			    logLeave("mediaquery");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return expr;
	}
	// $ANTLR end "mediaexpression"



	// $ANTLR start "inlineset"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:421:1: inlineset returns [RuleBlock<?> is] : ^( RULE (p= pseudo )* decl= declarations ) ;
	public final RuleBlock<?> inlineset() throws RecognitionException {
		RuleBlock<?> is = null;


		Selector.PseudoPage p =null;
		List<Declaration> decl =null;


		     logEnter("inlineset");
			 List<Selector.PseudoPage> pplist = new ArrayList<Selector.PseudoPage>();

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:429:2: ( ^( RULE (p= pseudo )* decl= declarations ) )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:429:4: ^( RULE (p= pseudo )* decl= declarations )
			{
			match(input,RULE,FOLLOW_RULE_in_inlineset752); 
			match(input, Token.DOWN, null); 
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:429:11: (p= pseudo )*
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( (LA16_0==PSEUDO) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:429:12: p= pseudo
					{
					pushFollow(FOLLOW_pseudo_in_inlineset757);
					p=pseudo();
					state._fsp--;

					pplist.add(p);
					}
					break;

				default :
					break loop16;
				}
			}

			pushFollow(FOLLOW_declarations_in_inlineset765);
			decl=declarations();
			state._fsp--;

			match(input, Token.UP, null); 

			 is = preparator.prepareInlineRuleSet(decl, pplist); 
			}


			     logLeave("inlineset");   

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return is;
	}
	// $ANTLR end "inlineset"



	// $ANTLR start "ruleset"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:438:1: ruleset returns [RuleBlock<?> stmnt] : ^( RULE (cs= combined_selector )* decl= declarations ) ;
	public final RuleBlock<?> ruleset() throws RecognitionException {
		RuleBlock<?> stmnt = null;


		CombinedSelector cs =null;
		List<Declaration> decl =null;


		    logEnter("ruleset"); 
		    List<CombinedSelector> cslist = new ArrayList<CombinedSelector>();

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:454:5: ( ^( RULE (cs= combined_selector )* decl= declarations ) )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:454:7: ^( RULE (cs= combined_selector )* decl= declarations )
			{
			match(input,RULE,FOLLOW_RULE_in_ruleset818); 
			match(input, Token.DOWN, null); 
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:455:9: (cs= combined_selector )*
			loop17:
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( (LA17_0==INVALID_SELECTOR||LA17_0==SELECTOR) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:455:10: cs= combined_selector
					{
					pushFollow(FOLLOW_combined_selector_in_ruleset832);
					cs=combined_selector();
					state._fsp--;

					if(cs!=null && !cs.isEmpty() && !statement_stack.peek().invalid) {
					            cslist.add(cs);
					            log.debug("Inserted combined selector ({}) into ruleset",  cslist.size());
					         }   
					        
					}
					break;

				default :
					break loop17;
				}
			}

			pushFollow(FOLLOW_declarations_in_ruleset853);
			decl=declarations();
			state._fsp--;

			match(input, Token.UP, null); 

			}


			    if(statement_stack.peek().invalid) {
			        stmnt = null;
			        log.debug("Ruleset not valid, so not created");
			    }
			    else {    
					 stmnt = preparator.prepareRuleSet(cslist, decl, (this.wrapMedia != null && !this.wrapMedia.isEmpty()), this.wrapMedia);
					 this.preventImports = true; 
			        }		
			    logLeave("ruleset");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return stmnt;
	}
	// $ANTLR end "ruleset"



	// $ANTLR start "declarations"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:468:1: declarations returns [List<Declaration> decl] : ^( SET (d= declaration )* ) ;
	public final List<Declaration> declarations() throws RecognitionException {
		List<Declaration> decl = null;


		Declaration d =null;


				  logEnter("declarations");
				  decl = new ArrayList<Declaration>();

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:476:2: ( ^( SET (d= declaration )* ) )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:476:4: ^( SET (d= declaration )* )
			{
			match(input,SET,FOLLOW_SET_in_declarations894); 
			if ( input.LA(1)==Token.DOWN ) {
				match(input, Token.DOWN, null); 
				// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:476:10: (d= declaration )*
				loop18:
				while (true) {
					int alt18=2;
					int LA18_0 = input.LA(1);
					if ( (LA18_0==DECLARATION||LA18_0==INVALID_DECLARATION) ) {
						alt18=1;
					}

					switch (alt18) {
					case 1 :
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:476:11: d= declaration
						{
						pushFollow(FOLLOW_declaration_in_declarations899);
						d=declaration();
						state._fsp--;


							     if(d!=null) {
						            decl.add(d);
						            log.debug("Inserted declaration #{} ", decl.size()+1);
								 }	
							 
						}
						break;

					default :
						break loop18;
					}
				}

				match(input, Token.UP, null); 
			}

			}


					   logLeave("declarations");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return decl;
	}
	// $ANTLR end "declarations"


	protected static class declaration_scope {
		Declaration d;
		boolean invalid;
	}
	protected Stack<declaration_scope> declaration_stack = new Stack<declaration_scope>();


	// $ANTLR start "declaration"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:489:1: declaration returns [Declaration decl] : ( ^( DECLARATION ( important )? ( INVALID_DIRECTIVE )? property t= terms ) | INVALID_DECLARATION );
	public final Declaration declaration() throws RecognitionException {
		declaration_stack.push(new declaration_scope());
		Declaration decl = null;


		List<Term<?>> t =null;


		    logEnter("declaration");
		    declaration_stack.peek().d = decl = rf.createDeclaration();
		    declaration_stack.peek().invalid = false;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:509:3: ( ^( DECLARATION ( important )? ( INVALID_DIRECTIVE )? property t= terms ) | INVALID_DECLARATION )
			int alt21=2;
			int LA21_0 = input.LA(1);
			if ( (LA21_0==DECLARATION) ) {
				alt21=1;
			}
			else if ( (LA21_0==INVALID_DECLARATION) ) {
				alt21=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 21, 0, input);
				throw nvae;
			}

			switch (alt21) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:509:5: ^( DECLARATION ( important )? ( INVALID_DIRECTIVE )? property t= terms )
					{
					match(input,DECLARATION,FOLLOW_DECLARATION_in_declaration943); 
					match(input, Token.DOWN, null); 
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:510:6: ( important )?
					int alt19=2;
					int LA19_0 = input.LA(1);
					if ( (LA19_0==IMPORTANT) ) {
						alt19=1;
					}
					switch (alt19) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:510:7: important
							{
							pushFollow(FOLLOW_important_in_declaration952);
							important();
							state._fsp--;

							 decl.setImportant(true); log.debug("IMPORTANT"); 
							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:511:7: ( INVALID_DIRECTIVE )?
					int alt20=2;
					int LA20_0 = input.LA(1);
					if ( (LA20_0==INVALID_DIRECTIVE) ) {
						alt20=1;
					}
					switch (alt20) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:511:8: INVALID_DIRECTIVE
							{
							match(input,INVALID_DIRECTIVE,FOLLOW_INVALID_DIRECTIVE_in_declaration965); 
							 declaration_stack.peek().invalid =true; 
							}
							break;

					}

					pushFollow(FOLLOW_property_in_declaration977);
					property();
					state._fsp--;

					pushFollow(FOLLOW_terms_in_declaration988);
					t=terms();
					state._fsp--;

					decl.replaceAll(t);
					match(input, Token.UP, null); 

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:515:4: INVALID_DECLARATION
					{
					match(input,INVALID_DECLARATION,FOLLOW_INVALID_DECLARATION_in_declaration1008); 
					 declaration_stack.peek().invalid =true;
					}
					break;

			}

			    if(declaration_stack.peek().invalid || declaration_stack.isEmpty()) {
			        decl =null;
			        log.debug("Declaration was invalidated or already invalid");
			    }
			    else {
			        log.debug("Returning declaration: {}.", decl);
			    }
			    logLeave("declaration");    

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
			declaration_stack.pop();
		}
		return decl;
	}
	// $ANTLR end "declaration"



	// $ANTLR start "important"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:518:1: important : IMPORTANT ;
	public final void important() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:519:5: ( IMPORTANT )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:519:7: IMPORTANT
			{
			match(input,IMPORTANT,FOLLOW_IMPORTANT_in_important1025); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "important"



	// $ANTLR start "property"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:525:1: property : (i= IDENT | MINUS i= IDENT );
	public final void property() throws RecognitionException {
		CommonTree i=null;


		    logEnter("property");

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:533:3: (i= IDENT | MINUS i= IDENT )
			int alt22=2;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==IDENT) ) {
				alt22=1;
			}
			else if ( (LA22_0==MINUS) ) {
				alt22=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 22, 0, input);
				throw nvae;
			}

			switch (alt22) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:533:5: i= IDENT
					{
					i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_property1065); 
					 declaration_stack.peek().d.setProperty(extractText(i)); declaration_stack.peek().d.setSource(extractSource(i)); 
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:534:5: MINUS i= IDENT
					{
					match(input,MINUS,FOLLOW_MINUS_in_property1073); 
					i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_property1079); 
					 declaration_stack.peek().d.setProperty("-" + extractText(i)); declaration_stack.peek().d.setSource(extractSource(i)); 
					}
					break;

			}

				log.debug("Setting property: {}", declaration_stack.peek().d.getProperty());	   
			    logLeave("property");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "property"


	protected static class terms_scope {
		List<Term<?>> list;
		Term<?> term;
		Term.Operator op;
		int unary;
		boolean dash;
	}
	protected Stack<terms_scope> terms_stack = new Stack<terms_scope>();


	// $ANTLR start "terms"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:540:1: terms returns [List<Term<?>> tlist] : ^( VALUE ( term )+ ) ;
	public final List<Term<?>> terms() throws RecognitionException {
		terms_stack.push(new terms_scope());
		List<Term<?>> tlist = null;



		    logEnter("terms");
		    terms_stack.peek().list = tlist = new ArrayList<Term<?>>();
		    terms_stack.peek().term = null;
		    terms_stack.peek().op = null;
		    terms_stack.peek().unary = 1;
		    terms_stack.peek().dash = false;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:560:5: ( ^( VALUE ( term )+ ) )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:560:7: ^( VALUE ( term )+ )
			{
			match(input,VALUE,FOLLOW_VALUE_in_terms1124); 
			match(input, Token.DOWN, null); 
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:560:15: ( term )+
			int cnt23=0;
			loop23:
			while (true) {
				int alt23=2;
				int LA23_0 = input.LA(1);
				if ( (LA23_0==ASTERISK||LA23_0==ATKEYWORD||LA23_0==BRACEBLOCK||(LA23_0 >= CLASSKEYWORD && LA23_0 <= COMMA)||(LA23_0 >= CURLYBLOCK && LA23_0 <= DASHMATCH)||LA23_0==DIMENSION||LA23_0==EQUALS||LA23_0==EXPRESSION||(LA23_0 >= FUNCTION && LA23_0 <= IDENT)||LA23_0==INCLUDES||LA23_0==INVALID_STRING||LA23_0==LESS||LA23_0==MINUS||LA23_0==NUMBER||(LA23_0 >= PARENBLOCK && LA23_0 <= PLUS)||LA23_0==QUESTION||LA23_0==SLASH||LA23_0==STRING||(LA23_0 >= UNIRANGE && LA23_0 <= URI)) ) {
					alt23=1;
				}

				switch (alt23) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:560:15: term
					{
					pushFollow(FOLLOW_term_in_terms1126);
					term();
					state._fsp--;

					}
					break;

				default :
					if ( cnt23 >= 1 ) break loop23;
					EarlyExitException eee = new EarlyExitException(23, input);
					throw eee;
				}
				cnt23++;
			}

			match(input, Token.UP, null); 

			}


				log.debug("Totally added {} terms", tlist.size());	   
			    logLeave("terms");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
			terms_stack.pop();
		}
		return tlist;
	}
	// $ANTLR end "terms"



	// $ANTLR start "term"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:563:1: term : ( valuepart | CURLYBLOCK | ATKEYWORD );
	public final void term() throws RecognitionException {

		  logEnter("term");

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:567:5: ( valuepart | CURLYBLOCK | ATKEYWORD )
			int alt24=3;
			switch ( input.LA(1) ) {
			case ASTERISK:
			case BRACEBLOCK:
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
			case LESS:
			case MINUS:
			case NUMBER:
			case PARENBLOCK:
			case PERCENT:
			case PERCENTAGE:
			case PLUS:
			case QUESTION:
			case SLASH:
			case STRING:
			case UNIRANGE:
			case URI:
				{
				alt24=1;
				}
				break;
			case CURLYBLOCK:
				{
				alt24=2;
				}
				break;
			case ATKEYWORD:
				{
				alt24=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 24, 0, input);
				throw nvae;
			}
			switch (alt24) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:567:7: valuepart
					{
					pushFollow(FOLLOW_valuepart_in_term1154);
					valuepart();
					state._fsp--;

					// set operator, store and create next 
					       if(!declaration_stack.peek().invalid && terms_stack.peek().term!=null) {
					          terms_stack.peek().term.setOperator(terms_stack.peek().op);
					          terms_stack.peek().list.add(terms_stack.peek().term);
					          // reinitialization
					          terms_stack.peek().op = Term.Operator.SPACE;
					          terms_stack.peek().unary = 1;
					          terms_stack.peek().dash = false;
					          terms_stack.peek().term = null;
					       }    
					      
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:579:7: CURLYBLOCK
					{
					match(input,CURLYBLOCK,FOLLOW_CURLYBLOCK_in_term1171); 
					 declaration_stack.peek().invalid = true;
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:580:7: ATKEYWORD
					{
					match(input,ATKEYWORD,FOLLOW_ATKEYWORD_in_term1181); 
					 declaration_stack.peek().invalid = true;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "term"



	// $ANTLR start "valuepart"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:583:1: valuepart : ( ( MINUS )? i= IDENT | CLASSKEYWORD | ( MINUS )? n= NUMBER | ( MINUS )? p= PERCENTAGE | ( MINUS )? d= DIMENSION |s= string |u= URI |h= HASH | UNIRANGE | INCLUDES | COLON | COMMA | GREATER | LESS | QUESTION | PERCENT | EQUALS | SLASH | PLUS | ASTERISK |e= EXPRESSION | ( MINUS )? ^(f= FUNCTION (t= terms )? ) | DASHMATCH | ^( PARENBLOCK ( any )* ) | ^( BRACEBLOCK ( any )* ) );
	public final void valuepart() throws RecognitionException {
		CommonTree i=null;
		CommonTree n=null;
		CommonTree p=null;
		CommonTree d=null;
		CommonTree u=null;
		CommonTree h=null;
		CommonTree e=null;
		CommonTree f=null;
		String s =null;
		List<Term<?>> t =null;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:605:5: ( ( MINUS )? i= IDENT | CLASSKEYWORD | ( MINUS )? n= NUMBER | ( MINUS )? p= PERCENTAGE | ( MINUS )? d= DIMENSION |s= string |u= URI |h= HASH | UNIRANGE | INCLUDES | COLON | COMMA | GREATER | LESS | QUESTION | PERCENT | EQUALS | SLASH | PLUS | ASTERISK |e= EXPRESSION | ( MINUS )? ^(f= FUNCTION (t= terms )? ) | DASHMATCH | ^( PARENBLOCK ( any )* ) | ^( BRACEBLOCK ( any )* ) )
			int alt33=25;
			switch ( input.LA(1) ) {
			case MINUS:
				{
				switch ( input.LA(2) ) {
				case IDENT:
					{
					alt33=1;
					}
					break;
				case NUMBER:
					{
					alt33=3;
					}
					break;
				case PERCENTAGE:
					{
					alt33=4;
					}
					break;
				case DIMENSION:
					{
					alt33=5;
					}
					break;
				case FUNCTION:
					{
					alt33=22;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 33, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case IDENT:
				{
				alt33=1;
				}
				break;
			case CLASSKEYWORD:
				{
				alt33=2;
				}
				break;
			case NUMBER:
				{
				alt33=3;
				}
				break;
			case PERCENTAGE:
				{
				alt33=4;
				}
				break;
			case DIMENSION:
				{
				alt33=5;
				}
				break;
			case INVALID_STRING:
			case STRING:
				{
				alt33=6;
				}
				break;
			case URI:
				{
				alt33=7;
				}
				break;
			case HASH:
				{
				alt33=8;
				}
				break;
			case UNIRANGE:
				{
				alt33=9;
				}
				break;
			case INCLUDES:
				{
				alt33=10;
				}
				break;
			case COLON:
				{
				alt33=11;
				}
				break;
			case COMMA:
				{
				alt33=12;
				}
				break;
			case GREATER:
				{
				alt33=13;
				}
				break;
			case LESS:
				{
				alt33=14;
				}
				break;
			case QUESTION:
				{
				alt33=15;
				}
				break;
			case PERCENT:
				{
				alt33=16;
				}
				break;
			case EQUALS:
				{
				alt33=17;
				}
				break;
			case SLASH:
				{
				alt33=18;
				}
				break;
			case PLUS:
				{
				alt33=19;
				}
				break;
			case ASTERISK:
				{
				alt33=20;
				}
				break;
			case EXPRESSION:
				{
				alt33=21;
				}
				break;
			case FUNCTION:
				{
				alt33=22;
				}
				break;
			case DASHMATCH:
				{
				alt33=23;
				}
				break;
			case PARENBLOCK:
				{
				alt33=24;
				}
				break;
			case BRACEBLOCK:
				{
				alt33=25;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 33, 0, input);
				throw nvae;
			}
			switch (alt33) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:605:7: ( MINUS )? i= IDENT
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:605:7: ( MINUS )?
					int alt25=2;
					int LA25_0 = input.LA(1);
					if ( (LA25_0==MINUS) ) {
						alt25=1;
					}
					switch (alt25) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:605:8: MINUS
							{
							match(input,MINUS,FOLLOW_MINUS_in_valuepart1208); 
							terms_stack.peek().dash =true;
							}
							break;

					}

					i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_valuepart1216); 
					terms_stack.peek().term = tf.createIdent(extractText(i), terms_stack.peek().dash);
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:606:7: CLASSKEYWORD
					{
					match(input,CLASSKEYWORD,FOLLOW_CLASSKEYWORD_in_valuepart1228); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:607:6: ( MINUS )? n= NUMBER
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:607:6: ( MINUS )?
					int alt26=2;
					int LA26_0 = input.LA(1);
					if ( (LA26_0==MINUS) ) {
						alt26=1;
					}
					switch (alt26) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:607:7: MINUS
							{
							match(input,MINUS,FOLLOW_MINUS_in_valuepart1238); 
							terms_stack.peek().unary =-1;
							}
							break;

					}

					n=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_valuepart1246); 
					terms_stack.peek().term = tf.createNumeric(extractText(n), terms_stack.peek().unary);
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:608:7: ( MINUS )? p= PERCENTAGE
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:608:7: ( MINUS )?
					int alt27=2;
					int LA27_0 = input.LA(1);
					if ( (LA27_0==MINUS) ) {
						alt27=1;
					}
					switch (alt27) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:608:8: MINUS
							{
							match(input,MINUS,FOLLOW_MINUS_in_valuepart1260); 
							terms_stack.peek().unary =-1;
							}
							break;

					}

					p=(CommonTree)match(input,PERCENTAGE,FOLLOW_PERCENTAGE_in_valuepart1268); 
					 terms_stack.peek().term = tf.createPercent(extractText(p), terms_stack.peek().unary);
					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:609:7: ( MINUS )? d= DIMENSION
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:609:7: ( MINUS )?
					int alt28=2;
					int LA28_0 = input.LA(1);
					if ( (LA28_0==MINUS) ) {
						alt28=1;
					}
					switch (alt28) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:609:8: MINUS
							{
							match(input,MINUS,FOLLOW_MINUS_in_valuepart1280); 
							terms_stack.peek().unary =-1;
							}
							break;

					}

					d=(CommonTree)match(input,DIMENSION,FOLLOW_DIMENSION_in_valuepart1288); 
					String dim = extractText(d);
									 terms_stack.peek().term = tf.createDimension(dim, terms_stack.peek().unary);
								     if(terms_stack.peek().term==null) {
										 log.info("Unable to create dimension from {}, unary {}", dim, terms_stack.peek().unary);
								         declaration_stack.peek().invalid = true;
									 }
						    
					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:617:7: s= string
					{
					pushFollow(FOLLOW_string_in_valuepart1306);
					s=string();
					state._fsp--;

					 if(s!=null) terms_stack.peek().term = tf.createString(s);
								  else declaration_stack.peek().invalid =true;
								
					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:621:7: u= URI
					{
					u=(CommonTree)match(input,URI,FOLLOW_URI_in_valuepart1325); 
					terms_stack.peek().term = tf.createURI(extractText(u), extractBase(u));
					}
					break;
				case 8 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:622:7: h= HASH
					{
					h=(CommonTree)match(input,HASH,FOLLOW_HASH_in_valuepart1343); 
					terms_stack.peek().term = tf.createColor(extractText(h));
						     if(terms_stack.peek().term==null)
						         declaration_stack.peek().invalid = true;
						    
					}
					break;
				case 9 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:627:7: UNIRANGE
					{
					match(input,UNIRANGE,FOLLOW_UNIRANGE_in_valuepart1362); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 10 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:628:7: INCLUDES
					{
					match(input,INCLUDES,FOLLOW_INCLUDES_in_valuepart1373); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 11 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:629:7: COLON
					{
					match(input,COLON,FOLLOW_COLON_in_valuepart1384); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 12 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:630:7: COMMA
					{
					match(input,COMMA,FOLLOW_COMMA_in_valuepart1398); 
					terms_stack.peek().op = Term.Operator.COMMA;
					}
					break;
				case 13 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:631:7: GREATER
					{
					match(input,GREATER,FOLLOW_GREATER_in_valuepart1416); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 14 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:632:7: LESS
					{
					match(input,LESS,FOLLOW_LESS_in_valuepart1428); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 15 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:633:7: QUESTION
					{
					match(input,QUESTION,FOLLOW_QUESTION_in_valuepart1443); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 16 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:634:7: PERCENT
					{
					match(input,PERCENT,FOLLOW_PERCENT_in_valuepart1454); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 17 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:635:7: EQUALS
					{
					match(input,EQUALS,FOLLOW_EQUALS_in_valuepart1466); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 18 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:636:7: SLASH
					{
					match(input,SLASH,FOLLOW_SLASH_in_valuepart1479); 
					terms_stack.peek().op = Term.Operator.SLASH;
					}
					break;
				case 19 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:637:5: PLUS
					{
					match(input,PLUS,FOLLOW_PLUS_in_valuepart1491); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 20 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:638:5: ASTERISK
					{
					match(input,ASTERISK,FOLLOW_ASTERISK_in_valuepart1502); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 21 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:639:5: e= EXPRESSION
					{
					e=(CommonTree)match(input,EXPRESSION,FOLLOW_EXPRESSION_in_valuepart1513); 

							    String exprval = extractText(e);
					        TermExpression expr = tf.createExpression(exprval.substring(11,exprval.length()-1)); //strip the 'expression()'
					        terms_stack.peek().term = expr;
							
					}
					break;
				case 22 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:644:7: ( MINUS )? ^(f= FUNCTION (t= terms )? )
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:644:7: ( MINUS )?
					int alt29=2;
					int LA29_0 = input.LA(1);
					if ( (LA29_0==MINUS) ) {
						alt29=1;
					}
					switch (alt29) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:644:8: MINUS
							{
							match(input,MINUS,FOLLOW_MINUS_in_valuepart1524); 
							terms_stack.peek().unary =-1;
							}
							break;

					}

					f=(CommonTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_valuepart1533); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:644:50: (t= terms )?
						int alt30=2;
						int LA30_0 = input.LA(1);
						if ( (LA30_0==VALUE) ) {
							alt30=1;
						}
						switch (alt30) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:644:50: t= terms
								{
								pushFollow(FOLLOW_terms_in_valuepart1537);
								t=terms();
								state._fsp--;

								}
								break;

						}

						match(input, Token.UP, null); 
					}


					        // create function
					        TermFunction function = tf.createFunction();
					        function.setFunctionName(extractText(f));
					        if (terms_stack.peek().unary == -1) //if started with minus, add the minus to the function name
					            function.setFunctionName('-' + function.getFunctionName());
					        if (t != null)
					        	function.setValue(t);
					        terms_stack.peek().term = function;
					    
					}
					break;
				case 23 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:654:7: DASHMATCH
					{
					match(input,DASHMATCH,FOLLOW_DASHMATCH_in_valuepart1549); 
					declaration_stack.peek().invalid = true;
					}
					break;
				case 24 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:655:7: ^( PARENBLOCK ( any )* )
					{
					match(input,PARENBLOCK,FOLLOW_PARENBLOCK_in_valuepart1560); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:655:20: ( any )*
						loop31:
						while (true) {
							int alt31=2;
							int LA31_0 = input.LA(1);
							if ( (LA31_0==BRACEBLOCK||(LA31_0 >= CLASSKEYWORD && LA31_0 <= COMMA)||LA31_0==DASHMATCH||LA31_0==DIMENSION||LA31_0==EQUALS||LA31_0==EXCLAMATION||(LA31_0 >= FUNCTION && LA31_0 <= IDENT)||LA31_0==INCLUDES||LA31_0==INVALID_STRING||LA31_0==NUMBER||LA31_0==PARENBLOCK||LA31_0==PERCENTAGE||LA31_0==SLASH||LA31_0==STRING||(LA31_0 >= UNIRANGE && LA31_0 <= URI)) ) {
								alt31=1;
							}

							switch (alt31) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:655:20: any
								{
								pushFollow(FOLLOW_any_in_valuepart1562);
								any();
								state._fsp--;

								}
								break;

							default :
								break loop31;
							}
						}

						match(input, Token.UP, null); 
					}

					declaration_stack.peek().invalid = true;
					}
					break;
				case 25 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:656:7: ^( BRACEBLOCK ( any )* )
					{
					match(input,BRACEBLOCK,FOLLOW_BRACEBLOCK_in_valuepart1575); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:656:20: ( any )*
						loop32:
						while (true) {
							int alt32=2;
							int LA32_0 = input.LA(1);
							if ( (LA32_0==BRACEBLOCK||(LA32_0 >= CLASSKEYWORD && LA32_0 <= COMMA)||LA32_0==DASHMATCH||LA32_0==DIMENSION||LA32_0==EQUALS||LA32_0==EXCLAMATION||(LA32_0 >= FUNCTION && LA32_0 <= IDENT)||LA32_0==INCLUDES||LA32_0==INVALID_STRING||LA32_0==NUMBER||LA32_0==PARENBLOCK||LA32_0==PERCENTAGE||LA32_0==SLASH||LA32_0==STRING||(LA32_0 >= UNIRANGE && LA32_0 <= URI)) ) {
								alt32=1;
							}

							switch (alt32) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:656:20: any
								{
								pushFollow(FOLLOW_any_in_valuepart1577);
								any();
								state._fsp--;

								}
								break;

							default :
								break loop32;
							}
						}

						match(input, Token.UP, null); 
					}

					declaration_stack.peek().invalid = true;
					}
					break;

			}

			    // convert color
			    Term<?> term = terms_stack.peek().term;
			    if(term!=null) {
			        TermColor colorTerm = null;
			        if(term instanceof TermIdent) {
			            colorTerm = tf.createColor((TermIdent)term);
			            if (colorTerm != null)
			                term = colorTerm;
			        }
			        else if(term instanceof TermFunction) {
			            colorTerm = tf.createColor((TermFunction)term);
			            if(colorTerm != null)
			                term = colorTerm;
			        }
			        // replace with color
			        if(colorTerm!=null) {
			            terms_stack.peek().term = colorTerm;
			        }                    
			    }

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "valuepart"


	protected static class combined_selector_scope {
		boolean invalid;
	}
	protected Stack<combined_selector_scope> combined_selector_stack = new Stack<combined_selector_scope>();


	// $ANTLR start "combined_selector"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:662:1: combined_selector returns [CombinedSelector combinedSelector] : s= selector (c= combinator s= selector )* ;
	public final CombinedSelector combined_selector() throws RecognitionException {
		combined_selector_stack.push(new combined_selector_scope());
		CombinedSelector combinedSelector = null;


		Selector s =null;
		Selector.Combinator c =null;


			logEnter("combined_selector");	  
			combinedSelector = (CombinedSelector) rf.createCombinedSelector().unlock();

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:689:2: (s= selector (c= combinator s= selector )* )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:689:4: s= selector (c= combinator s= selector )*
			{
			pushFollow(FOLLOW_selector_in_combined_selector1625);
			s=selector();
			state._fsp--;


				     combinedSelector.add(s);
				  
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:692:3: (c= combinator s= selector )*
			loop34:
			while (true) {
				int alt34=2;
				int LA34_0 = input.LA(1);
				if ( (LA34_0==ADJACENT||LA34_0==CHILD||LA34_0==DESCENDANT||LA34_0==PRECEDING) ) {
					alt34=1;
				}

				switch (alt34) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:692:4: c= combinator s= selector
					{
					pushFollow(FOLLOW_combinator_in_combined_selector1634);
					c=combinator();
					state._fsp--;

					pushFollow(FOLLOW_selector_in_combined_selector1638);
					s=selector();
					state._fsp--;


						     s.setCombinator(c);
						     combinedSelector.add(s);	
						  
					}
					break;

				default :
					break loop34;
				}
			}

			}

			  
			    // entire ruleset is not valid when selector is not valid
			    // there is no need to parse selector's when already marked as invalid
			    if(statement_stack.peek().invalid || combined_selector_stack.peek().invalid) {        
			        combinedSelector = null;
			        if(statement_stack.peek().invalid) { 
						log.debug("Ommiting combined selector, whole statement discarded");
					}	
			        else { 
						log.debug("Combined selector is invalid");               
			        }
					// mark whole ruleset as invalid
			        statement_stack.peek().invalid = true;
			    }
			    else {
			        log.debug("Returing combined selector: {}.", combinedSelector); 
			    }
			    logLeave("combined_selector"); 

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
			combined_selector_stack.pop();
		}
		return combinedSelector;
	}
	// $ANTLR end "combined_selector"



	// $ANTLR start "combinator"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:699:1: combinator returns [Selector.Combinator combinator] : ( CHILD | ADJACENT | PRECEDING | DESCENDANT );
	public final Selector.Combinator combinator() throws RecognitionException {
		Selector.Combinator combinator = null;


		 logEnter("combinator"); 
		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:702:2: ( CHILD | ADJACENT | PRECEDING | DESCENDANT )
			int alt35=4;
			switch ( input.LA(1) ) {
			case CHILD:
				{
				alt35=1;
				}
				break;
			case ADJACENT:
				{
				alt35=2;
				}
				break;
			case PRECEDING:
				{
				alt35=3;
				}
				break;
			case DESCENDANT:
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
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:702:4: CHILD
					{
					match(input,CHILD,FOLLOW_CHILD_in_combinator1668); 
					combinator =Selector.Combinator.CHILD;
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:703:4: ADJACENT
					{
					match(input,ADJACENT,FOLLOW_ADJACENT_in_combinator1675); 
					combinator =Selector.Combinator.ADJACENT;
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:704:5: PRECEDING
					{
					match(input,PRECEDING,FOLLOW_PRECEDING_in_combinator1683); 
					combinator =Selector.Combinator.PRECEDING;
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:705:4: DESCENDANT
					{
					match(input,DESCENDANT,FOLLOW_DESCENDANT_in_combinator1690); 
					combinator =Selector.Combinator.DESCENDANT;
					}
					break;

			}
			 logLeave("combinator"); 
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return combinator;
	}
	// $ANTLR end "combinator"


	protected static class selector_scope {
		Selector s;
	}
	protected Stack<selector_scope> selector_stack = new Stack<selector_scope>();


	// $ANTLR start "selector"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:709:1: selector returns [Selector sel] : ( ^( SELECTOR ^( ELEMENT (i= IDENT )? ) ( selpart )* ) | ^( SELECTOR ( selpart )+ ) | INVALID_SELECTOR );
	public final Selector selector() throws RecognitionException {
		selector_stack.push(new selector_scope());
		Selector sel = null;


		CommonTree i=null;


			logEnter("selector");
			selector_stack.peek().s =sel=(Selector)rf.createSelector().unlock();
			Selector.ElementName en = rf.createElement(Selector.ElementName.WILDCARD);

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:721:5: ( ^( SELECTOR ^( ELEMENT (i= IDENT )? ) ( selpart )* ) | ^( SELECTOR ( selpart )+ ) | INVALID_SELECTOR )
			int alt39=3;
			int LA39_0 = input.LA(1);
			if ( (LA39_0==SELECTOR) ) {
				int LA39_1 = input.LA(2);
				if ( (LA39_1==DOWN) ) {
					int LA39_3 = input.LA(3);
					if ( (LA39_3==ELEMENT) ) {
						alt39=1;
					}
					else if ( (LA39_3==ATTRIBUTE||LA39_3==CLASSKEYWORD||LA39_3==HASH||LA39_3==INVALID_SELPART||LA39_3==PSEUDO) ) {
						alt39=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 39, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 39, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA39_0==INVALID_SELECTOR) ) {
				alt39=3;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 39, 0, input);
				throw nvae;
			}

			switch (alt39) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:721:7: ^( SELECTOR ^( ELEMENT (i= IDENT )? ) ( selpart )* )
					{
					match(input,SELECTOR,FOLLOW_SELECTOR_in_selector1726); 
					match(input, Token.DOWN, null); 
					match(input,ELEMENT,FOLLOW_ELEMENT_in_selector1738); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:723:11: (i= IDENT )?
						int alt36=2;
						int LA36_0 = input.LA(1);
						if ( (LA36_0==IDENT) ) {
							alt36=1;
						}
						switch (alt36) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:723:12: i= IDENT
								{
								i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_selector1754); 
								 en.setName(extractText(i)); 
								}
								break;

						}

						match(input, Token.UP, null); 
					}


							  log.debug("Adding element name: {}.", en.getName());
							  selector_stack.peek().s.add(en);
							 
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:729:10: ( selpart )*
					loop37:
					while (true) {
						int alt37=2;
						int LA37_0 = input.LA(1);
						if ( (LA37_0==ATTRIBUTE||LA37_0==CLASSKEYWORD||LA37_0==HASH||LA37_0==INVALID_SELPART||LA37_0==PSEUDO) ) {
							alt37=1;
						}

						switch (alt37) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:729:10: selpart
							{
							pushFollow(FOLLOW_selpart_in_selector1801);
							selpart();
							state._fsp--;

							}
							break;

						default :
							break loop37;
						}
					}

					match(input, Token.UP, null); 

					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:731:7: ^( SELECTOR ( selpart )+ )
					{
					match(input,SELECTOR,FOLLOW_SELECTOR_in_selector1820); 
					match(input, Token.DOWN, null); 
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:732:10: ( selpart )+
					int cnt38=0;
					loop38:
					while (true) {
						int alt38=2;
						int LA38_0 = input.LA(1);
						if ( (LA38_0==ATTRIBUTE||LA38_0==CLASSKEYWORD||LA38_0==HASH||LA38_0==INVALID_SELPART||LA38_0==PSEUDO) ) {
							alt38=1;
						}

						switch (alt38) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:732:10: selpart
							{
							pushFollow(FOLLOW_selpart_in_selector1832);
							selpart();
							state._fsp--;

							}
							break;

						default :
							if ( cnt38 >= 1 ) break loop38;
							EarlyExitException eee = new EarlyExitException(38, input);
							throw eee;
						}
						cnt38++;
					}

					match(input, Token.UP, null); 

					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:734:7: INVALID_SELECTOR
					{
					match(input,INVALID_SELECTOR,FOLLOW_INVALID_SELECTOR_in_selector1850); 
					 statement_stack.peek().invalid = true; 
					}
					break;

			}

				logLeave("selector");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
			selector_stack.pop();
		}
		return sel;
	}
	// $ANTLR end "selector"



	// $ANTLR start "selpart"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:737:1: selpart : (h= HASH |c= CLASSKEYWORD | ^( ATTRIBUTE ea= attribute ) |p= pseudo | INVALID_SELPART );
	public final void selpart() throws RecognitionException {
		CommonTree h=null;
		CommonTree c=null;
		Selector.ElementAttribute ea =null;
		Selector.PseudoPage p =null;


			logEnter("selpart");

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:744:5: (h= HASH |c= CLASSKEYWORD | ^( ATTRIBUTE ea= attribute ) |p= pseudo | INVALID_SELPART )
			int alt40=5;
			switch ( input.LA(1) ) {
			case HASH:
				{
				alt40=1;
				}
				break;
			case CLASSKEYWORD:
				{
				alt40=2;
				}
				break;
			case ATTRIBUTE:
				{
				alt40=3;
				}
				break;
			case PSEUDO:
				{
				alt40=4;
				}
				break;
			case INVALID_SELPART:
				{
				alt40=5;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 40, 0, input);
				throw nvae;
			}
			switch (alt40) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:744:8: h= HASH
					{
					h=(CommonTree)match(input,HASH,FOLLOW_HASH_in_selpart1884); 
					 selector_stack.peek().s.add(rf.createID(extractText(h))); 
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:745:7: c= CLASSKEYWORD
					{
					c=(CommonTree)match(input,CLASSKEYWORD,FOLLOW_CLASSKEYWORD_in_selpart1896); 
					 selector_stack.peek().s.add(rf.createClass(extractText(c))); 
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:746:4: ^( ATTRIBUTE ea= attribute )
					{
					match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_selpart1904); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_attribute_in_selpart1908);
					ea=attribute();
					state._fsp--;

					 selector_stack.peek().s.add(ea);
					match(input, Token.UP, null); 

					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:747:7: p= pseudo
					{
					pushFollow(FOLLOW_pseudo_in_selpart1922);
					p=pseudo();
					state._fsp--;

					 selector_stack.peek().s.add(p);
					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:748:4: INVALID_SELPART
					{
					match(input,INVALID_SELPART,FOLLOW_INVALID_SELPART_in_selpart1929); 
					 combined_selector_stack.peek().invalid = true;
					}
					break;

			}

			    logLeave("selpart");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "selpart"



	// $ANTLR start "attribute"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:751:1: attribute returns [Selector.ElementAttribute elemAttr] : i= IDENT ( ( EQUALS | INCLUDES | DASHMATCH | CONTAINS | STARTSWITH | ENDSWITH ) (i= IDENT |s= string ) )? ;
	public final Selector.ElementAttribute attribute() throws RecognitionException {
		Selector.ElementAttribute elemAttr = null;


		CommonTree i=null;
		String s =null;


		    logEnter("attribute");
		    String attribute = null;
			String value = null;
			Selector.Operator op = Selector.Operator.NO_OPERATOR;
			boolean isStringValue = false;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:769:2: (i= IDENT ( ( EQUALS | INCLUDES | DASHMATCH | CONTAINS | STARTSWITH | ENDSWITH ) (i= IDENT |s= string ) )? )
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:769:4: i= IDENT ( ( EQUALS | INCLUDES | DASHMATCH | CONTAINS | STARTSWITH | ENDSWITH ) (i= IDENT |s= string ) )?
			{
			i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_attribute1963); 
			attribute=extractText(i); 
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:770:4: ( ( EQUALS | INCLUDES | DASHMATCH | CONTAINS | STARTSWITH | ENDSWITH ) (i= IDENT |s= string ) )?
			int alt43=2;
			int LA43_0 = input.LA(1);
			if ( (LA43_0==CONTAINS||LA43_0==DASHMATCH||(LA43_0 >= ENDSWITH && LA43_0 <= EQUALS)||LA43_0==INCLUDES||LA43_0==STARTSWITH) ) {
				alt43=1;
			}
			switch (alt43) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:770:5: ( EQUALS | INCLUDES | DASHMATCH | CONTAINS | STARTSWITH | ENDSWITH ) (i= IDENT |s= string )
					{
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:770:5: ( EQUALS | INCLUDES | DASHMATCH | CONTAINS | STARTSWITH | ENDSWITH )
					int alt41=6;
					switch ( input.LA(1) ) {
					case EQUALS:
						{
						alt41=1;
						}
						break;
					case INCLUDES:
						{
						alt41=2;
						}
						break;
					case DASHMATCH:
						{
						alt41=3;
						}
						break;
					case CONTAINS:
						{
						alt41=4;
						}
						break;
					case STARTSWITH:
						{
						alt41=5;
						}
						break;
					case ENDSWITH:
						{
						alt41=6;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 41, 0, input);
						throw nvae;
					}
					switch (alt41) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:770:6: EQUALS
							{
							match(input,EQUALS,FOLLOW_EQUALS_in_attribute1972); 
							op=Selector.Operator.EQUALS; 
							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:771:7: INCLUDES
							{
							match(input,INCLUDES,FOLLOW_INCLUDES_in_attribute1983); 
							op=Selector.Operator.INCLUDES; 
							}
							break;
						case 3 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:772:7: DASHMATCH
							{
							match(input,DASHMATCH,FOLLOW_DASHMATCH_in_attribute1994); 
							op=Selector.Operator.DASHMATCH; 
							}
							break;
						case 4 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:773:8: CONTAINS
							{
							match(input,CONTAINS,FOLLOW_CONTAINS_in_attribute2005); 
							op=Selector.Operator.CONTAINS; 
							}
							break;
						case 5 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:774:8: STARTSWITH
							{
							match(input,STARTSWITH,FOLLOW_STARTSWITH_in_attribute2016); 
							op=Selector.Operator.STARTSWITH; 
							}
							break;
						case 6 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:775:8: ENDSWITH
							{
							match(input,ENDSWITH,FOLLOW_ENDSWITH_in_attribute2027); 
							op=Selector.Operator.ENDSWITH; 
							}
							break;

					}

					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:777:5: (i= IDENT |s= string )
					int alt42=2;
					int LA42_0 = input.LA(1);
					if ( (LA42_0==IDENT) ) {
						alt42=1;
					}
					else if ( (LA42_0==INVALID_STRING||LA42_0==STRING) ) {
						alt42=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 42, 0, input);
						throw nvae;
					}

					switch (alt42) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:777:6: i= IDENT
							{
							i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_attribute2045); 

									value=extractText(i);
									isStringValue=false;
									
							}
							break;
						case 2 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:781:7: s= string
							{
							pushFollow(FOLLOW_string_in_attribute2057);
							s=string();
							state._fsp--;


									 if(s!=null)  { 
										value=s;
										isStringValue=true;
									 }	
									 else {
										combined_selector_stack.peek().invalid =true;
									 }
									
							}
							break;

					}

					}
					break;

			}

			}


			    if(attribute!=null) {
					elemAttr = rf.createAttribute(value, isStringValue, op, attribute);
				}
				else {
				    log.debug("Invalid attribute element in selector");
				    combined_selector_stack.peek().invalid = true;
				}
			    logLeave("attribute");

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return elemAttr;
	}
	// $ANTLR end "attribute"



	// $ANTLR start "pseudo"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:793:1: pseudo returns [Selector.PseudoPage pseudoPage] : ( ^( PSEUDO i= IDENT ) | ^( PSEUDO f= FUNCTION i= IDENT ) | ^( PSEUDO f= FUNCTION (m= MINUS )? n= NUMBER ) | ^( PSEUDO f= FUNCTION (m= MINUS )? n= INDEX ) );
	public final Selector.PseudoPage pseudo() throws RecognitionException {
		Selector.PseudoPage pseudoPage = null;


		CommonTree i=null;
		CommonTree f=null;
		CommonTree m=null;
		CommonTree n=null;


				logEnter("pseudo");

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:797:2: ( ^( PSEUDO i= IDENT ) | ^( PSEUDO f= FUNCTION i= IDENT ) | ^( PSEUDO f= FUNCTION (m= MINUS )? n= NUMBER ) | ^( PSEUDO f= FUNCTION (m= MINUS )? n= INDEX ) )
			int alt46=4;
			int LA46_0 = input.LA(1);
			if ( (LA46_0==PSEUDO) ) {
				int LA46_1 = input.LA(2);
				if ( (LA46_1==DOWN) ) {
					int LA46_2 = input.LA(3);
					if ( (LA46_2==IDENT) ) {
						alt46=1;
					}
					else if ( (LA46_2==FUNCTION) ) {
						switch ( input.LA(4) ) {
						case IDENT:
							{
							alt46=2;
							}
							break;
						case MINUS:
							{
							int LA46_6 = input.LA(5);
							if ( (LA46_6==NUMBER) ) {
								alt46=3;
							}
							else if ( (LA46_6==INDEX) ) {
								alt46=4;
							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 46, 6, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

							}
							break;
						case NUMBER:
							{
							alt46=3;
							}
							break;
						case INDEX:
							{
							alt46=4;
							}
							break;
						default:
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 46, 4, input);
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
								new NoViableAltException("", 46, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 46, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 46, 0, input);
				throw nvae;
			}

			switch (alt46) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:797:4: ^( PSEUDO i= IDENT )
					{
					match(input,PSEUDO,FOLLOW_PSEUDO_in_pseudo2090); 
					match(input, Token.DOWN, null); 
					i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_pseudo2094); 
					match(input, Token.UP, null); 


								pseudoPage = rf.createPseudoPage(extractText(i), null);
							
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:801:4: ^( PSEUDO f= FUNCTION i= IDENT )
					{
					match(input,PSEUDO,FOLLOW_PSEUDO_in_pseudo2105); 
					match(input, Token.DOWN, null); 
					f=(CommonTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_pseudo2109); 
					i=(CommonTree)match(input,IDENT,FOLLOW_IDENT_in_pseudo2113); 
					match(input, Token.UP, null); 


								pseudoPage = rf.createPseudoPage(extractText(i), extractText(f));
							
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:805:4: ^( PSEUDO f= FUNCTION (m= MINUS )? n= NUMBER )
					{
					match(input,PSEUDO,FOLLOW_PSEUDO_in_pseudo2124); 
					match(input, Token.DOWN, null); 
					f=(CommonTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_pseudo2128); 
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:805:25: (m= MINUS )?
					int alt44=2;
					int LA44_0 = input.LA(1);
					if ( (LA44_0==MINUS) ) {
						alt44=1;
					}
					switch (alt44) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:805:25: m= MINUS
							{
							m=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_pseudo2132); 
							}
							break;

					}

					n=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_pseudo2137); 
					match(input, Token.UP, null); 


					      String exp = extractText(n);
					      if (m != null) exp = "-" + exp;
								pseudoPage = rf.createPseudoPage(exp, extractText(f));
							
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:811:5: ^( PSEUDO f= FUNCTION (m= MINUS )? n= INDEX )
					{
					match(input,PSEUDO,FOLLOW_PSEUDO_in_pseudo2149); 
					match(input, Token.DOWN, null); 
					f=(CommonTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_pseudo2153); 
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:811:26: (m= MINUS )?
					int alt45=2;
					int LA45_0 = input.LA(1);
					if ( (LA45_0==MINUS) ) {
						alt45=1;
					}
					switch (alt45) {
						case 1 :
							// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:811:26: m= MINUS
							{
							m=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_pseudo2157); 
							}
							break;

					}

					n=(CommonTree)match(input,INDEX,FOLLOW_INDEX_in_pseudo2162); 
					match(input, Token.UP, null); 


					      String exp = extractText(n);
					      if (m != null) exp = "-" + exp;
					      pseudoPage = rf.createPseudoPage(exp, extractText(f));
					    
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return pseudoPage;
	}
	// $ANTLR end "pseudo"



	// $ANTLR start "string"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:819:1: string returns [String s] : (st= STRING | INVALID_STRING );
	public final String string() throws RecognitionException {
		String s = null;


		CommonTree st=null;

		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:820:2: (st= STRING | INVALID_STRING )
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( (LA47_0==STRING) ) {
				alt47=1;
			}
			else if ( (LA47_0==INVALID_STRING) ) {
				alt47=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 47, 0, input);
				throw nvae;
			}

			switch (alt47) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:820:4: st= STRING
					{
					st=(CommonTree)match(input,STRING,FOLLOW_STRING_in_string2186); 
					 s = extractText(st);
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:821:4: INVALID_STRING
					{
					match(input,INVALID_STRING,FOLLOW_INVALID_STRING_in_string2193); 
					s =null;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return s;
	}
	// $ANTLR end "string"



	// $ANTLR start "any"
	// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:824:1: any : ( IDENT | CLASSKEYWORD | NUMBER | PERCENTAGE | DIMENSION | string | URI | HASH | UNIRANGE | INCLUDES | COLON | COMMA | GREATER | EQUALS | SLASH | EXCLAMATION | ^( FUNCTION ( any )* ) | DASHMATCH | ^( PARENBLOCK ( any )* ) | ^( BRACEBLOCK ( any )* ) );
	public final void any() throws RecognitionException {
		try {
			// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:825:3: ( IDENT | CLASSKEYWORD | NUMBER | PERCENTAGE | DIMENSION | string | URI | HASH | UNIRANGE | INCLUDES | COLON | COMMA | GREATER | EQUALS | SLASH | EXCLAMATION | ^( FUNCTION ( any )* ) | DASHMATCH | ^( PARENBLOCK ( any )* ) | ^( BRACEBLOCK ( any )* ) )
			int alt51=20;
			switch ( input.LA(1) ) {
			case IDENT:
				{
				alt51=1;
				}
				break;
			case CLASSKEYWORD:
				{
				alt51=2;
				}
				break;
			case NUMBER:
				{
				alt51=3;
				}
				break;
			case PERCENTAGE:
				{
				alt51=4;
				}
				break;
			case DIMENSION:
				{
				alt51=5;
				}
				break;
			case INVALID_STRING:
			case STRING:
				{
				alt51=6;
				}
				break;
			case URI:
				{
				alt51=7;
				}
				break;
			case HASH:
				{
				alt51=8;
				}
				break;
			case UNIRANGE:
				{
				alt51=9;
				}
				break;
			case INCLUDES:
				{
				alt51=10;
				}
				break;
			case COLON:
				{
				alt51=11;
				}
				break;
			case COMMA:
				{
				alt51=12;
				}
				break;
			case GREATER:
				{
				alt51=13;
				}
				break;
			case EQUALS:
				{
				alt51=14;
				}
				break;
			case SLASH:
				{
				alt51=15;
				}
				break;
			case EXCLAMATION:
				{
				alt51=16;
				}
				break;
			case FUNCTION:
				{
				alt51=17;
				}
				break;
			case DASHMATCH:
				{
				alt51=18;
				}
				break;
			case PARENBLOCK:
				{
				alt51=19;
				}
				break;
			case BRACEBLOCK:
				{
				alt51=20;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 51, 0, input);
				throw nvae;
			}
			switch (alt51) {
				case 1 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:825:5: IDENT
					{
					match(input,IDENT,FOLLOW_IDENT_in_any2209); 
					}
					break;
				case 2 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:826:5: CLASSKEYWORD
					{
					match(input,CLASSKEYWORD,FOLLOW_CLASSKEYWORD_in_any2215); 
					}
					break;
				case 3 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:827:5: NUMBER
					{
					match(input,NUMBER,FOLLOW_NUMBER_in_any2221); 
					}
					break;
				case 4 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:828:5: PERCENTAGE
					{
					match(input,PERCENTAGE,FOLLOW_PERCENTAGE_in_any2227); 
					}
					break;
				case 5 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:829:5: DIMENSION
					{
					match(input,DIMENSION,FOLLOW_DIMENSION_in_any2233); 
					}
					break;
				case 6 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:830:5: string
					{
					pushFollow(FOLLOW_string_in_any2239);
					string();
					state._fsp--;

					}
					break;
				case 7 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:831:5: URI
					{
					match(input,URI,FOLLOW_URI_in_any2245); 
					}
					break;
				case 8 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:832:5: HASH
					{
					match(input,HASH,FOLLOW_HASH_in_any2251); 
					}
					break;
				case 9 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:833:5: UNIRANGE
					{
					match(input,UNIRANGE,FOLLOW_UNIRANGE_in_any2257); 
					}
					break;
				case 10 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:834:5: INCLUDES
					{
					match(input,INCLUDES,FOLLOW_INCLUDES_in_any2263); 
					}
					break;
				case 11 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:835:5: COLON
					{
					match(input,COLON,FOLLOW_COLON_in_any2269); 
					}
					break;
				case 12 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:836:5: COMMA
					{
					match(input,COMMA,FOLLOW_COMMA_in_any2275); 
					}
					break;
				case 13 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:837:5: GREATER
					{
					match(input,GREATER,FOLLOW_GREATER_in_any2281); 
					}
					break;
				case 14 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:838:5: EQUALS
					{
					match(input,EQUALS,FOLLOW_EQUALS_in_any2287); 
					}
					break;
				case 15 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:839:5: SLASH
					{
					match(input,SLASH,FOLLOW_SLASH_in_any2293); 
					}
					break;
				case 16 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:840:5: EXCLAMATION
					{
					match(input,EXCLAMATION,FOLLOW_EXCLAMATION_in_any2299); 
					}
					break;
				case 17 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:841:5: ^( FUNCTION ( any )* )
					{
					match(input,FUNCTION,FOLLOW_FUNCTION_in_any2306); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:841:16: ( any )*
						loop48:
						while (true) {
							int alt48=2;
							int LA48_0 = input.LA(1);
							if ( (LA48_0==BRACEBLOCK||(LA48_0 >= CLASSKEYWORD && LA48_0 <= COMMA)||LA48_0==DASHMATCH||LA48_0==DIMENSION||LA48_0==EQUALS||LA48_0==EXCLAMATION||(LA48_0 >= FUNCTION && LA48_0 <= IDENT)||LA48_0==INCLUDES||LA48_0==INVALID_STRING||LA48_0==NUMBER||LA48_0==PARENBLOCK||LA48_0==PERCENTAGE||LA48_0==SLASH||LA48_0==STRING||(LA48_0 >= UNIRANGE && LA48_0 <= URI)) ) {
								alt48=1;
							}

							switch (alt48) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:841:16: any
								{
								pushFollow(FOLLOW_any_in_any2308);
								any();
								state._fsp--;

								}
								break;

							default :
								break loop48;
							}
						}

						match(input, Token.UP, null); 
					}

					}
					break;
				case 18 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:842:5: DASHMATCH
					{
					match(input,DASHMATCH,FOLLOW_DASHMATCH_in_any2317); 
					}
					break;
				case 19 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:843:5: ^( PARENBLOCK ( any )* )
					{
					match(input,PARENBLOCK,FOLLOW_PARENBLOCK_in_any2324); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:843:18: ( any )*
						loop49:
						while (true) {
							int alt49=2;
							int LA49_0 = input.LA(1);
							if ( (LA49_0==BRACEBLOCK||(LA49_0 >= CLASSKEYWORD && LA49_0 <= COMMA)||LA49_0==DASHMATCH||LA49_0==DIMENSION||LA49_0==EQUALS||LA49_0==EXCLAMATION||(LA49_0 >= FUNCTION && LA49_0 <= IDENT)||LA49_0==INCLUDES||LA49_0==INVALID_STRING||LA49_0==NUMBER||LA49_0==PARENBLOCK||LA49_0==PERCENTAGE||LA49_0==SLASH||LA49_0==STRING||(LA49_0 >= UNIRANGE && LA49_0 <= URI)) ) {
								alt49=1;
							}

							switch (alt49) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:843:18: any
								{
								pushFollow(FOLLOW_any_in_any2326);
								any();
								state._fsp--;

								}
								break;

							default :
								break loop49;
							}
						}

						match(input, Token.UP, null); 
					}

					}
					break;
				case 20 :
					// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:844:5: ^( BRACEBLOCK ( any )* )
					{
					match(input,BRACEBLOCK,FOLLOW_BRACEBLOCK_in_any2335); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:844:18: ( any )*
						loop50:
						while (true) {
							int alt50=2;
							int LA50_0 = input.LA(1);
							if ( (LA50_0==BRACEBLOCK||(LA50_0 >= CLASSKEYWORD && LA50_0 <= COMMA)||LA50_0==DASHMATCH||LA50_0==DIMENSION||LA50_0==EQUALS||LA50_0==EXCLAMATION||(LA50_0 >= FUNCTION && LA50_0 <= IDENT)||LA50_0==INCLUDES||LA50_0==INVALID_STRING||LA50_0==NUMBER||LA50_0==PARENBLOCK||LA50_0==PERCENTAGE||LA50_0==SLASH||LA50_0==STRING||(LA50_0 >= UNIRANGE && LA50_0 <= URI)) ) {
								alt50=1;
							}

							switch (alt50) {
							case 1 :
								// cz/vutbr/web/csskit/antlr/CSSTreeParser.g:844:18: any
								{
								pushFollow(FOLLOW_any_in_any2337);
								any();
								state._fsp--;

								}
								break;

							default :
								break loop50;
							}
						}

						match(input, Token.UP, null); 
					}

					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "any"

	// Delegated rules



	public static final BitSet FOLLOW_INLINESTYLE_in_inlinestyle59 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_declarations_in_inlinestyle63 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_INLINESTYLE_in_inlinestyle78 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_inlineset_in_inlinestyle88 = new BitSet(new long[]{0x0000000000000008L,0x0000000000010000L});
	public static final BitSet FOLLOW_STYLESHEET_in_stylesheet125 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_statement_in_stylesheet134 = new BitSet(new long[]{0x0404808200002008L,0x0000000400010010L});
	public static final BitSet FOLLOW_ruleset_in_statement183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_atstatement_in_statement193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INVALID_STATEMENT_in_statement200 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CHARSET_in_atstatement233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INVALID_IMPORT_in_atstatement239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IMPORT_in_atstatement246 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_media_in_atstatement258 = new BitSet(new long[]{0x0000000000000000L,0x0000000041000000L});
	public static final BitSet FOLLOW_import_uri_in_atstatement272 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_PAGE_in_atstatement291 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_IDENT_in_atstatement302 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100400L});
	public static final BitSet FOLLOW_PSEUDO_in_atstatement331 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_IDENT_in_atstatement335 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_declarations_in_atstatement365 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_SET_in_atstatement374 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_margin_in_atstatement379 = new BitSet(new long[]{0x0200000000000008L});
	public static final BitSet FOLLOW_VIEWPORT_in_atstatement403 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_declarations_in_atstatement407 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_FONTFACE_in_atstatement421 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_declarations_in_atstatement425 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_MEDIA_in_atstatement438 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_media_in_atstatement443 = new BitSet(new long[]{0x0004000000000008L,0x0000000000010000L});
	public static final BitSet FOLLOW_ruleset_in_atstatement456 = new BitSet(new long[]{0x0004000000000008L,0x0000000000010000L});
	public static final BitSet FOLLOW_INVALID_STATEMENT_in_atstatement467 = new BitSet(new long[]{0x0004000000000008L,0x0000000000010000L});
	public static final BitSet FOLLOW_URI_in_import_uri511 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_import_uri523 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MARGIN_AREA_in_margin557 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_declarations_in_margin563 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_mediaquery_in_media599 = new BitSet(new long[]{0x0800000000000002L});
	public static final BitSet FOLLOW_MEDIA_QUERY_in_mediaquery635 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_mediaterm_in_mediaquery637 = new BitSet(new long[]{0x0004202001000008L});
	public static final BitSet FOLLOW_IDENT_in_mediaterm655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_mediaexpression_in_mediaterm675 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INVALID_STATEMENT_in_mediaterm686 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_declaration_in_mediaexpression721 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RULE_in_inlineset752 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_pseudo_in_inlineset757 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100400L});
	public static final BitSet FOLLOW_declarations_in_inlineset765 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_RULE_in_ruleset818 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_combined_selector_in_ruleset832 = new BitSet(new long[]{0x0001000000000000L,0x0000000000140000L});
	public static final BitSet FOLLOW_declarations_in_ruleset853 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_SET_in_declarations894 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_declaration_in_declarations899 = new BitSet(new long[]{0x0000200001000008L});
	public static final BitSet FOLLOW_DECLARATION_in_declaration943 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_important_in_declaration952 = new BitSet(new long[]{0x1000402000000000L});
	public static final BitSet FOLLOW_INVALID_DIRECTIVE_in_declaration965 = new BitSet(new long[]{0x1000002000000000L});
	public static final BitSet FOLLOW_property_in_declaration977 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_terms_in_declaration988 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_INVALID_DECLARATION_in_declaration1008 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IMPORTANT_in_important1025 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENT_in_property1065 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_property1073 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_IDENT_in_property1079 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VALUE_in_terms1124 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_term_in_terms1126 = new BitSet(new long[]{0x1088023D24C38548L,0x00000000612009E4L});
	public static final BitSet FOLLOW_valuepart_in_term1154 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CURLYBLOCK_in_term1171 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ATKEYWORD_in_term1181 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1208 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_IDENT_in_valuepart1216 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CLASSKEYWORD_in_valuepart1228 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1238 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_NUMBER_in_valuepart1246 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1260 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_PERCENTAGE_in_valuepart1268 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1280 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_DIMENSION_in_valuepart1288 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_in_valuepart1306 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_URI_in_valuepart1325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HASH_in_valuepart1343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_UNIRANGE_in_valuepart1362 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INCLUDES_in_valuepart1373 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLON_in_valuepart1384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COMMA_in_valuepart1398 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GREATER_in_valuepart1416 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LESS_in_valuepart1428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QUESTION_in_valuepart1443 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PERCENT_in_valuepart1454 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EQUALS_in_valuepart1466 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SLASH_in_valuepart1479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PLUS_in_valuepart1491 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ASTERISK_in_valuepart1502 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EXPRESSION_in_valuepart1513 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_valuepart1524 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_FUNCTION_in_valuepart1533 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_terms_in_valuepart1537 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_DASHMATCH_in_valuepart1549 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PARENBLOCK_in_valuepart1560 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_any_in_valuepart1562 = new BitSet(new long[]{0x0008023CA4838408L,0x00000000612000A4L});
	public static final BitSet FOLLOW_BRACEBLOCK_in_valuepart1575 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_any_in_valuepart1577 = new BitSet(new long[]{0x0008023CA4838408L,0x00000000612000A4L});
	public static final BitSet FOLLOW_selector_in_combined_selector1625 = new BitSet(new long[]{0x0000000002004012L,0x0000000000000200L});
	public static final BitSet FOLLOW_combinator_in_combined_selector1634 = new BitSet(new long[]{0x0001000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_selector_in_combined_selector1638 = new BitSet(new long[]{0x0000000002004012L,0x0000000000000200L});
	public static final BitSet FOLLOW_CHILD_in_combinator1668 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ADJACENT_in_combinator1675 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PRECEDING_in_combinator1683 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DESCENDANT_in_combinator1690 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SELECTOR_in_selector1726 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ELEMENT_in_selector1738 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_IDENT_in_selector1754 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_selpart_in_selector1801 = new BitSet(new long[]{0x0002001000008208L,0x0000000000000400L});
	public static final BitSet FOLLOW_SELECTOR_in_selector1820 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_selpart_in_selector1832 = new BitSet(new long[]{0x0002001000008208L,0x0000000000000400L});
	public static final BitSet FOLLOW_INVALID_SELECTOR_in_selector1850 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HASH_in_selpart1884 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CLASSKEYWORD_in_selpart1896 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ATTRIBUTE_in_selpart1904 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_attribute_in_selpart1908 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_pseudo_in_selpart1922 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INVALID_SELPART_in_selpart1929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENT_in_attribute1963 = new BitSet(new long[]{0x0000020030880002L,0x0000000000800000L});
	public static final BitSet FOLLOW_EQUALS_in_attribute1972 = new BitSet(new long[]{0x0008002000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_INCLUDES_in_attribute1983 = new BitSet(new long[]{0x0008002000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_DASHMATCH_in_attribute1994 = new BitSet(new long[]{0x0008002000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_CONTAINS_in_attribute2005 = new BitSet(new long[]{0x0008002000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_STARTSWITH_in_attribute2016 = new BitSet(new long[]{0x0008002000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_ENDSWITH_in_attribute2027 = new BitSet(new long[]{0x0008002000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_IDENT_in_attribute2045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_in_attribute2057 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PSEUDO_in_pseudo2090 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_IDENT_in_pseudo2094 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_PSEUDO_in_pseudo2105 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_FUNCTION_in_pseudo2109 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_IDENT_in_pseudo2113 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_PSEUDO_in_pseudo2124 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_FUNCTION_in_pseudo2128 = new BitSet(new long[]{0x1000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_MINUS_in_pseudo2132 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_NUMBER_in_pseudo2137 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_PSEUDO_in_pseudo2149 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_FUNCTION_in_pseudo2153 = new BitSet(new long[]{0x1000040000000000L});
	public static final BitSet FOLLOW_MINUS_in_pseudo2157 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_INDEX_in_pseudo2162 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_STRING_in_string2186 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INVALID_STRING_in_string2193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENT_in_any2209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CLASSKEYWORD_in_any2215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NUMBER_in_any2221 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PERCENTAGE_in_any2227 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DIMENSION_in_any2233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_in_any2239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_URI_in_any2245 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HASH_in_any2251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_UNIRANGE_in_any2257 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INCLUDES_in_any2263 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLON_in_any2269 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COMMA_in_any2275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GREATER_in_any2281 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EQUALS_in_any2287 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SLASH_in_any2293 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EXCLAMATION_in_any2299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNCTION_in_any2306 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_any_in_any2308 = new BitSet(new long[]{0x0008023CA4838408L,0x00000000612000A4L});
	public static final BitSet FOLLOW_DASHMATCH_in_any2317 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PARENBLOCK_in_any2324 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_any_in_any2326 = new BitSet(new long[]{0x0008023CA4838408L,0x00000000612000A4L});
	public static final BitSet FOLLOW_BRACEBLOCK_in_any2335 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_any_in_any2337 = new BitSet(new long[]{0x0008023CA4838408L,0x00000000612000A4L});
}
