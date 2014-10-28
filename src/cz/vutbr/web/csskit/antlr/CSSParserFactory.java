/*
 * Copyright (C) kapy
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */
package cz.vutbr.web.csskit.antlr;

import com.intellij.psi.PsiElement;
import cz.vutbr.web.css.*;
import cz.vutbr.web.css.RuleBlock.Priority;
import cz.vutbr.web.csskit.PriorityStrategy;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.fit.net.DataURLHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles construction of parser
 * 
 * @author kapy
 * 
 */
public class CSSParserFactory {
	private static final Logger log = LoggerFactory
			.getLogger(CSSParserFactory.class);

	/**
	 * Last priority obtained from parsing. Next stylesheet will be started with this priority
	 */
	private static Priority lastPriority = null;
	
	/**
	 * Encapsulates functionality associated with different source types.
	 * 
	 * @author kapy
	 * 
	 */
	public static enum SourceType {
		INLINE {
			@Override
			public CommonTree getAST(CSSParser parser) throws CSSException {
				try {
					CSSParser.inlinestyle_return retval = parser.inlinestyle();
					return (CommonTree) retval.getTree();
				} catch (RecognitionException re) {
					throw encapsulateException(re,
							"Unable to parse inline CSS style");
				} catch (RuntimeException re) {
					throw encapsulateException(re,
							"Unable to parse inline CSS style");
				}
			}

			@Override
			public RuleList parse(CSSTreeParser parser) throws CSSException {
				try {
					return parser.inlinestyle();
				} catch (RecognitionException re) {
					throw encapsulateException(re,
							"Unable to parse inline CSS style [AST]");
				} catch (RuntimeException re) {
					throw encapsulateException(re,
							"Unable to parse inline CSS style [AST]");
				}
			}

			@Override
			public CSSInputStream getInput(Object source, String encoding) throws IOException {
				return CSSInputStream.stringStream((String) source);
			}

		},
		EMBEDDED {
			@Override
			public CommonTree getAST(CSSParser parser) throws CSSException {
				try {
					CSSParser.stylesheet_return retval = parser.stylesheet();
					return (CommonTree) retval.getTree();
				} catch (RecognitionException re) {
					throw encapsulateException(re,
							"Unable to parse embedded CSS style");
				} catch (RuntimeException re) {
					throw encapsulateException(re,
							"Unable to parse embedded CSS style");
				}
			}

			@Override
			public RuleList parse(CSSTreeParser parser) throws CSSException {
				try {
					return parser.stylesheet();
				} catch (RecognitionException re) {
					throw encapsulateException(re,
							"Unable to parse embedded CSS style [AST]");
				} catch (RuntimeException re) {
					throw encapsulateException(re,
							"Unable to parse embedded CSS style [AST]");
				}
			}

			@Override
			public CSSInputStream getInput(Object source, String encoding) throws IOException {
				return CSSInputStream.stringStream((String) source);
			}

		},
		URL {
			@Override
			public CommonTree getAST(CSSParser parser) throws CSSException {
				try {
					CSSParser.stylesheet_return retval = parser.stylesheet();
					return (CommonTree) retval.getTree();
				} catch (RecognitionException re) {
					throw encapsulateException(re,
							"Unable to parse URL CSS style");
				} catch (RuntimeException re) {
					throw encapsulateException(re,
							"Unable to parse URL CSS style");
				}
			}

			@Override
			public RuleList parse(CSSTreeParser parser) throws CSSException {
				try {
					return parser.stylesheet();
				} catch (RecognitionException re) {
					throw encapsulateException(re,
							"Unable to parse file CSS style [AST]");
				} catch (RuntimeException re) {
					throw encapsulateException(re,
							"Unable to parse file CSS style [AST]");
				}
			}

			@Override
			public CSSInputStream getInput(Object source, String encoding) throws IOException {
				return CSSInputStream.urlStream((URL) source, encoding);
			}

		};

		/**
		 * Creates input for CSSLexer
		 * 
		 * @param source
		 *            Source, either raw data (String) or URL 
		 * @return Created stream
		 * @throws IOException
		 *             When file is not found or other IO exception occurs
		 */
		public abstract CSSInputStream getInput(Object source, String encoding)
				throws IOException;

		/**
		 * Creates AST tree for CSSTreeParser
		 * 
		 * @param parser
		 *            Source parser
		 * @return Created AST tree
		 * @throws CSSException
		 *             When unrecoverable exception during parse occurs.
		 *             RuntimeException are also encapsulated at this point
		 */
		public abstract CommonTree getAST(CSSParser parser) throws CSSException;

		/**
		 * Creates StyleSheet from AST tree
		 * 
		 * @param parser
		 *            Parser
		 * @return Created StyleSheet
		 * @throws CSSException
		 *             When unrecoverable exception during parse occurs.
		 *             RuntimeException are also encapsulated at this point
		 */
		public abstract RuleList parse(CSSTreeParser parser)
				throws CSSException;

		/**
		 * Creates new CSSException which encapsulates cause
		 * 
		 * @param t
		 *            Cause
		 * @param msg
		 *            Message
		 * @return New CSSException
		 */
		private static CSSException encapsulateException(Throwable t, String msg) {
			log.error("THROWN:", t);
			return new CSSException(msg, t);
		}
	}

    //========================================================================================================================
	
	// disable instantiation
	private CSSParserFactory() {
		throw new AssertionError();
	}

	/**
	 * Parses source of given type
	 * 
	 * @param source
	 *            Source, interpretation depends on {@code type}
	 * @param type
	 *            Type of source provided
	 * @param inline
	 *            InlineElement
     * @param inlinePriority
     *            True when the rule should have an 'inline' (greater) priority
	 * @return Created StyleSheet
	 * @throws IOException
	 *             When problem with input stream occurs
	 * @throws CSSException
	 *             When unrecoverable exception during parsing occurs
	 */
	public static StyleSheet parse(Object source, String encoding, SourceType type,
			PsiElement inline, boolean inlinePriority, URL base) throws IOException, CSSException {

		StyleSheet sheet = (StyleSheet) CSSFactory.getRuleFactory()
				.createStyleSheet().unlock();

		PriorityStrategy ps = new AtomicPriorityStrategy(lastPriority);
		Preparator preparator = new SimplePreparator(inline, inlinePriority);
        StyleSheet ret = parseAndImport(source, encoding, type, sheet, preparator, ps, base, null);
		lastPriority = ret.getLastMark();
		return ret;
	}

	/**
	 * Parses source of given type. Uses no element.
	 * 
	 * @param source
	 *            Source, interpretation depends on {@code type}
	 * @param type
	 *            Type of source provided
	 * @param base
	 *            The base URL
	 * @return Created StyleSheet
	 * @throws IOException
	 *             When problem with input stream occurs
	 * @throws CSSException
	 *             When unrecoverable exception during parsing occurs
	 * @throws IllegalArgumentException
	 *             When type of source is INLINE
	 */
	public static StyleSheet parse(Object source, String encoding, SourceType type, URL base)
			throws IOException, CSSException {
		if (type == SourceType.INLINE)
			throw new IllegalArgumentException(
					"Missing element for INLINE input");

		return parse(source, encoding, type, null, false, base);
	}

	/**
	 * Appends parsed source to passed style sheet. This style sheet must be
	 * IMPERATIVELY parsed by this factory to guarantee proper appending
	 * 
	 * @param source
	 *            Source, interpretation depends on {@code type}
	 * @param type
	 *            Type of source provided
	 * @param inline
	 *            Inline element
	 * @param inlinePriority
	 *            True when the rule should have an 'inline' (greater) priority
	 * @param sheet
	 *            StyleSheet to be modified
	 * @return Modified StyleSheet
	 * @throws IOException
	 *             When problem with input stream occurs
	 * @throws CSSException
	 *             When unrecoverable exception during parsing occurs
	 */
	public static StyleSheet append(Object source, String encoding, SourceType type,
			PsiElement inline, boolean inlinePriority, StyleSheet sheet, URL base) throws IOException, CSSException {

	    Priority start = sheet.getLastMark();
	    if (start == null)
	        start = lastPriority;
		PriorityStrategy ps = new AtomicPriorityStrategy(start);
		Preparator preparator = new SimplePreparator(inline, inlinePriority);
		StyleSheet ret = parseAndImport(source, encoding, type, sheet, preparator, ps, base, null);
		lastPriority = ret.getLastMark();
		return ret;
	}

	/**
	 * Appends parsed source to passed style sheet. This style sheet must be
	 * IMPERATIVELY parsed by this factory to guarantee proper appending. Uses
	 * no inline element
	 * 
	 * @param source
	 *            Source, interpretation depends on {@code type}
	 * @param type
	 *            Type of source provided
	 * @param base
	 *            Base url
	 * @param sheet
	 *            StyleSheet to be modified
	 * @return Modified StyleSheet
	 * @throws IOException
	 *             When problem with input stream occurs
	 * @throws CSSException
	 *             When unrecoverable exception during parsing occurs
	 * @throws IllegalArgumentException
	 *             When type of source is INLINE
	 */
	public static StyleSheet append(Object source, String encoding, SourceType type,
			StyleSheet sheet, URL base) throws IOException, CSSException {
		if (type == SourceType.INLINE)
			throw new IllegalArgumentException(
					"Missing element for INLINE input");

		return append(source, encoding, type, null, false, sheet, base);
	}
	
	/**
	 * Resets the rule priority to the initial state (completely new parsing)
	 */
	public static void resetPriority()
	{
	    lastPriority = null;
	}
	
	/**
	 * Parses the source using the given infrastructure and returns the resulting style sheet.
	 * The imports are handled recursively.
	 */
	private static StyleSheet parseAndImport(Object source, String encoding, SourceType type,
	        StyleSheet sheet, Preparator preparator, PriorityStrategy ps, URL base, List<MediaQuery> media)
	        throws CSSException, IOException
	{
        CSSTreeParser parser = createTreeParser(source, encoding, type, preparator, base, media);
        type.parse(parser);
        
        for (int i = 0; i < parser.getImportPaths().size(); i++)
        {
            String path = parser.getImportPaths().get(i);
            List<MediaQuery> imedia = parser.getImportMedia().get(i);
            
            if (((imedia == null || imedia.isEmpty()) && CSSFactory.getAutoImportMedia().matchesEmpty()) //no media query specified
                 || CSSFactory.getAutoImportMedia().matchesOneOf(imedia)) //or some media query matches to the autoload media spec
            {    
                URL url = DataURLHandler.createURL(base, path);
                try {
                    parseAndImport(url, encoding, SourceType.URL, sheet, preparator, ps, url, imedia);
                } catch (IOException e) {
                    log.warn("Couldn't read imported style sheet: {}", e.getMessage());
                }
            }
            else
                log.trace("Skipping import {} (media not matching)", path);
        }

	    return parser.addRulesToStyleSheet(sheet, ps);
	}
	
	// creates the tree parser
	private static CSSTreeParser createTreeParser(Object source, String encoding, SourceType type,
			Preparator preparator, URL base, List<MediaQuery> media) throws IOException, CSSException {

		CSSInputStream input = type.getInput(source, encoding);
		input.setBase(base);
		CommonTokenStream tokens = feedLexer(input);
		CommonTree ast = feedParser(tokens, type);
		return feedAST(tokens, ast, preparator, media);
	}

	// initializer lexer
	private static CommonTokenStream feedLexer(CSSInputStream source)
	        throws CSSException 
	{
		// we have to unpack runtime exception
		// because of Java limitation
		// to change method contract with different type of exception
		try {
			CSSLexer lexer = new CSSLexer(source);
			lexer.init();
			return new CommonTokenStream(lexer);
		} catch (RuntimeException re) {
			if (re.getCause() instanceof CSSException) {
				throw (CSSException) re.getCause();
			}
			// this is some other exception
			else {
				log.error("LEXER THROWS:", re);
				throw re;
			}
		}
	}

	// Initializes parser
	private static CommonTree feedParser(CommonTokenStream source, SourceType type)
	        throws CSSException 
	{
		CSSParser parser = new CSSParser(source);
		parser.init();
		return type.getAST(parser);
	}

	// initializes tree parser
	private static CSSTreeParser feedAST(CommonTokenStream source, CommonTree ast, Preparator preparator, List<MediaQuery> media) 
	{
		if (log.isTraceEnabled()) {
			log.trace("Feeding tree parser with AST:\n{}", TreeUtil.toStringTree(ast));
		}
		// Walk resulting tree; create tree-node stream first
		CommonTreeNodeStream nodes = new CommonTreeNodeStream(ast);
		// AST nodes have payloads that point into token stream
		nodes.setTokenStream(source);
		CSSTreeParser parser = new CSSTreeParser(nodes);
		return parser.init(preparator, media);
	}

    //========================================================================================================================
	
	/**
	 * Parses a media query from a string (e.g. the 'media' HTML attribute).
	 * @param query The query string
	 * @return List of media queries found.
	 */
	public static List<MediaQuery> parseMediaQuery(String query)
	{
	    try
        {
	        //input from string
            CSSInputStream input = CSSInputStream.stringStream(query);
            input.setBase(new URL("file://media/query/url")); //this URL should not be used, just for safety
            //lexer
            CommonTokenStream tokens = feedLexer(input);
            //run parser - create AST
            CSSParser parser = new CSSParser(tokens);
            parser.init();
            CSSParser.media_return retval = parser.media();
            CommonTree ast = (CommonTree) retval.getTree();
            //tree parser
            CSSTreeParser tparser = feedAST(tokens, ast, null, null);
            return tparser.media();
        } catch (IOException e) {
            log.error("I/O error during media query parsing: {}", e.getMessage());
            return null;
        } catch (CSSException e) {
            log.warn("Malformed media query {}", query);
            return null;
        } catch (RecognitionException e) {
            log.warn("Malformed media query {}", query);
            return null;
        }
	}
	
	//========================================================================================================================
	
	// priority strategy using atomic incrementing
	private static final class AtomicPriorityStrategy implements
			PriorityStrategy {

		private final AtomicInteger counter;

		public AtomicPriorityStrategy(Priority last) {
			if (last == null)
				this.counter = new AtomicInteger(0);
			else if (!(last instanceof PriorityImpl))
				throw new ClassCastException(
						"Unable to continue with priority class provided: "
								+ last.getClass());
			else
				this.counter = new AtomicInteger(((PriorityImpl) last).priority);
		}

		public Priority getAndIncrement() {
			return new PriorityImpl(counter.incrementAndGet());
		}

		public Priority markAndIncrement() {
			return new PriorityImpl(counter.incrementAndGet());
		}

	}

	// not atomic priority strategy
	@SuppressWarnings("unused")
	private static final class SimplePriorityStrategy implements
			PriorityStrategy {

		private int counter;

		public SimplePriorityStrategy(Priority last) {
			if (!(last instanceof PriorityImpl))
				throw new ClassCastException(
						"Unable to continue with priority class provided: "
								+ last.getClass());

			this.counter = ((PriorityImpl) last).priority;
		}

		public Priority getAndIncrement() {
			return new PriorityImpl(counter++);
		}

		public Priority markAndIncrement() {
			return new PriorityImpl(counter++);
		}
	}

	// priority using integer value
	private static final class PriorityImpl implements Priority {

		final int priority;

		public PriorityImpl(int priority) {
			this.priority = priority;
		}

		public int compareTo(Priority o) {
			/*if (!(o instanceof PriorityImpl))
				throw new ClassCastException(
						"Unable to compare with different instance of priority: "
								+ o.getClass());*/

			PriorityImpl other = (PriorityImpl) o;

			return this.priority - other.priority;
		}

		@Override
		public String toString() {
			return String.valueOf(priority);
		}
	}

}
