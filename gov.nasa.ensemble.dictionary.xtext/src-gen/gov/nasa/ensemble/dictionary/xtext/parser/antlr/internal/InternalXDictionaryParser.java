package gov.nasa.ensemble.dictionary.xtext.parser.antlr.internal; 

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import gov.nasa.ensemble.dictionary.xtext.services.XDictionaryGrammarAccess;



import org.antlr.runtime.*;

@SuppressWarnings("all")
public class InternalXDictionaryParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'name'", "'='", "'author'", "'date'", "'description'", "'version'", "'domain'", "'Enum'", "'{'", "'}'", "'Literal'", "'color'", "'literal'", "'attribute'", "'defaultValueLiteral'", "'shortDescription'", "'units'", "'displayName'", "'category'", "'parameterName'", "'reference'", "'containment'", "'true'", "'false'", "'ActivityDef'", "'duration'", "'hidden'", "'annotation'", "'numericRequirement'", "'expression'", "'stateRequirement'", "'definition'", "'requiredState'", "'numericEffect'", "'stateEffect'", "'claimableEffect'", "'sharedEffect'", "'ActivityGroupDef'", "'ObjectDef'", "'NumericResource'", "'StateResource'", "'ClaimableResource'", "'SharableResource'", "'Summaryresource'"
    };
    public static final int RULE_ID=4;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int RULE_ANY_OTHER=10;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int T__51=51;
    public static final int T__16=16;
    public static final int T__52=52;
    public static final int T__15=15;
    public static final int T__53=53;
    public static final int T__18=18;
    public static final int T__54=54;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int RULE_INT=6;
    public static final int T__50=50;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int RULE_SL_COMMENT=8;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int RULE_STRING=5;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int RULE_WS=9;

    // delegates
    // delegators


        public InternalXDictionaryParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalXDictionaryParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    @Override
	public String[] getTokenNames() { return InternalXDictionaryParser.tokenNames; }
    @Override
	public String getGrammarFileName() { return "../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g"; }



     	private XDictionaryGrammarAccess grammarAccess;
     	
        public InternalXDictionaryParser(TokenStream input, XDictionaryGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "Dictionary";	
       	}
       	
       	@Override
       	protected XDictionaryGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleDictionary"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:67:1: entryRuleDictionary returns [EObject current=null] : iv_ruleDictionary= ruleDictionary EOF ;
    public final EObject entryRuleDictionary() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDictionary = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:68:2: (iv_ruleDictionary= ruleDictionary EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:69:2: iv_ruleDictionary= ruleDictionary EOF
            {
             newCompositeNode(grammarAccess.getDictionaryRule()); 
            pushFollow(FOLLOW_ruleDictionary_in_entryRuleDictionary75);
            iv_ruleDictionary=ruleDictionary();

            state._fsp--;

             current =iv_ruleDictionary; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDictionary85); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDictionary"


    // $ANTLR start "ruleDictionary"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:76:1: ruleDictionary returns [EObject current=null] : ( ( ( ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ( (lv_definitions_19_0= ruleDefinition ) )* ) ;
    public final EObject ruleDictionary() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token lv_author_6_0=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token lv_date_9_0=null;
        Token otherlv_10=null;
        Token otherlv_11=null;
        Token lv_description_12_0=null;
        Token otherlv_13=null;
        Token otherlv_14=null;
        Token lv_version_15_0=null;
        Token otherlv_16=null;
        Token otherlv_17=null;
        Token lv_domain_18_0=null;
        EObject lv_definitions_19_0 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:79:28: ( ( ( ( ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ( (lv_definitions_19_0= ruleDefinition ) )* ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:80:1: ( ( ( ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ( (lv_definitions_19_0= ruleDefinition ) )* )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:80:1: ( ( ( ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ( (lv_definitions_19_0= ruleDefinition ) )* )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:80:2: ( ( ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ( (lv_definitions_19_0= ruleDefinition ) )*
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:80:2: ( ( ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:82:1: ( ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:82:1: ( ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:83:2: ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?)
            {
             
            	  getUnorderedGroupHelper().enter(grammarAccess.getDictionaryAccess().getUnorderedGroup_0());
            	
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:86:2: ( ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?)
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:87:3: ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+ {...}?
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:87:3: ( ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) ) )+
            int cnt1=0;
            loop1:
            do {
                int alt1=7;
                int LA1_0 = input.LA(1);

                if ( LA1_0 ==11 && getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 0) ) {
                    alt1=1;
                }
                else if ( LA1_0 ==13 && getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 1) ) {
                    alt1=2;
                }
                else if ( LA1_0 ==14 && getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 2) ) {
                    alt1=3;
                }
                else if ( LA1_0 ==15 && getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 3) ) {
                    alt1=4;
                }
                else if ( LA1_0 ==16 && getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 4) ) {
                    alt1=5;
                }
                else if ( LA1_0 ==17 && getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 5) ) {
                    alt1=6;
                }


                switch (alt1) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:89:4: ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:89:4: ({...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:90:5: {...}? => ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 0)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:90:107: ( ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:91:6: ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 0);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:94:6: ({...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:94:7: {...}? => (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:94:16: (otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:94:18: otherlv_1= 'name' otherlv_2= '=' ( (lv_name_3_0= RULE_ID ) )
            	    {
            	    otherlv_1=(Token)match(input,11,FOLLOW_11_in_ruleDictionary168); 

            	        	newLeafNode(otherlv_1, grammarAccess.getDictionaryAccess().getNameKeyword_0_0_0());
            	        
            	    otherlv_2=(Token)match(input,12,FOLLOW_12_in_ruleDictionary180); 

            	        	newLeafNode(otherlv_2, grammarAccess.getDictionaryAccess().getEqualsSignKeyword_0_0_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:102:1: ( (lv_name_3_0= RULE_ID ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:103:1: (lv_name_3_0= RULE_ID )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:103:1: (lv_name_3_0= RULE_ID )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:104:3: lv_name_3_0= RULE_ID
            	    {
            	    lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleDictionary197); 

            	    			newLeafNode(lv_name_3_0, grammarAccess.getDictionaryAccess().getNameIDTerminalRuleCall_0_0_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getDictionaryRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"name",
            	            		lv_name_3_0, 
            	            		"ID");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getDictionaryAccess().getUnorderedGroup_0());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:127:4: ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:127:4: ({...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:128:5: {...}? => ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 1)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:128:107: ( ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:129:6: ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 1);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:132:6: ({...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:132:7: {...}? => (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:132:16: (otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:132:18: otherlv_4= 'author' otherlv_5= '=' ( (lv_author_6_0= RULE_STRING ) )
            	    {
            	    otherlv_4=(Token)match(input,13,FOLLOW_13_in_ruleDictionary270); 

            	        	newLeafNode(otherlv_4, grammarAccess.getDictionaryAccess().getAuthorKeyword_0_1_0());
            	        
            	    otherlv_5=(Token)match(input,12,FOLLOW_12_in_ruleDictionary282); 

            	        	newLeafNode(otherlv_5, grammarAccess.getDictionaryAccess().getEqualsSignKeyword_0_1_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:140:1: ( (lv_author_6_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:141:1: (lv_author_6_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:141:1: (lv_author_6_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:142:3: lv_author_6_0= RULE_STRING
            	    {
            	    lv_author_6_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleDictionary299); 

            	    			newLeafNode(lv_author_6_0, grammarAccess.getDictionaryAccess().getAuthorSTRINGTerminalRuleCall_0_1_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getDictionaryRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"author",
            	            		lv_author_6_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getDictionaryAccess().getUnorderedGroup_0());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 3 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:165:4: ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:165:4: ({...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:166:5: {...}? => ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 2) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 2)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:166:107: ( ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:167:6: ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 2);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:170:6: ({...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:170:7: {...}? => (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:170:16: (otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:170:18: otherlv_7= 'date' otherlv_8= '=' ( (lv_date_9_0= RULE_STRING ) )
            	    {
            	    otherlv_7=(Token)match(input,14,FOLLOW_14_in_ruleDictionary372); 

            	        	newLeafNode(otherlv_7, grammarAccess.getDictionaryAccess().getDateKeyword_0_2_0());
            	        
            	    otherlv_8=(Token)match(input,12,FOLLOW_12_in_ruleDictionary384); 

            	        	newLeafNode(otherlv_8, grammarAccess.getDictionaryAccess().getEqualsSignKeyword_0_2_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:178:1: ( (lv_date_9_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:179:1: (lv_date_9_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:179:1: (lv_date_9_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:180:3: lv_date_9_0= RULE_STRING
            	    {
            	    lv_date_9_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleDictionary401); 

            	    			newLeafNode(lv_date_9_0, grammarAccess.getDictionaryAccess().getDateSTRINGTerminalRuleCall_0_2_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getDictionaryRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"date",
            	            		lv_date_9_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getDictionaryAccess().getUnorderedGroup_0());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 4 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:203:4: ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:203:4: ({...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:204:5: {...}? => ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 3) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 3)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:204:107: ( ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:205:6: ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 3);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:208:6: ({...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:208:7: {...}? => (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:208:16: (otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:208:18: otherlv_10= 'description' otherlv_11= '=' ( (lv_description_12_0= RULE_STRING ) )
            	    {
            	    otherlv_10=(Token)match(input,15,FOLLOW_15_in_ruleDictionary474); 

            	        	newLeafNode(otherlv_10, grammarAccess.getDictionaryAccess().getDescriptionKeyword_0_3_0());
            	        
            	    otherlv_11=(Token)match(input,12,FOLLOW_12_in_ruleDictionary486); 

            	        	newLeafNode(otherlv_11, grammarAccess.getDictionaryAccess().getEqualsSignKeyword_0_3_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:216:1: ( (lv_description_12_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:217:1: (lv_description_12_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:217:1: (lv_description_12_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:218:3: lv_description_12_0= RULE_STRING
            	    {
            	    lv_description_12_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleDictionary503); 

            	    			newLeafNode(lv_description_12_0, grammarAccess.getDictionaryAccess().getDescriptionSTRINGTerminalRuleCall_0_3_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getDictionaryRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"description",
            	            		lv_description_12_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getDictionaryAccess().getUnorderedGroup_0());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 5 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:241:4: ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:241:4: ({...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:242:5: {...}? => ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 4) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 4)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:242:107: ( ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:243:6: ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 4);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:246:6: ({...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:246:7: {...}? => (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:246:16: (otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:246:18: otherlv_13= 'version' otherlv_14= '=' ( (lv_version_15_0= RULE_STRING ) )
            	    {
            	    otherlv_13=(Token)match(input,16,FOLLOW_16_in_ruleDictionary576); 

            	        	newLeafNode(otherlv_13, grammarAccess.getDictionaryAccess().getVersionKeyword_0_4_0());
            	        
            	    otherlv_14=(Token)match(input,12,FOLLOW_12_in_ruleDictionary588); 

            	        	newLeafNode(otherlv_14, grammarAccess.getDictionaryAccess().getEqualsSignKeyword_0_4_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:254:1: ( (lv_version_15_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:255:1: (lv_version_15_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:255:1: (lv_version_15_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:256:3: lv_version_15_0= RULE_STRING
            	    {
            	    lv_version_15_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleDictionary605); 

            	    			newLeafNode(lv_version_15_0, grammarAccess.getDictionaryAccess().getVersionSTRINGTerminalRuleCall_0_4_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getDictionaryRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"version",
            	            		lv_version_15_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getDictionaryAccess().getUnorderedGroup_0());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 6 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:279:4: ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:279:4: ({...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:280:5: {...}? => ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 5) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "getUnorderedGroupHelper().canSelect(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 5)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:280:107: ( ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:281:6: ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getDictionaryAccess().getUnorderedGroup_0(), 5);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:284:6: ({...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:284:7: {...}? => (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleDictionary", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:284:16: (otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:284:18: otherlv_16= 'domain' otherlv_17= '=' ( (lv_domain_18_0= RULE_STRING ) )
            	    {
            	    otherlv_16=(Token)match(input,17,FOLLOW_17_in_ruleDictionary678); 

            	        	newLeafNode(otherlv_16, grammarAccess.getDictionaryAccess().getDomainKeyword_0_5_0());
            	        
            	    otherlv_17=(Token)match(input,12,FOLLOW_12_in_ruleDictionary690); 

            	        	newLeafNode(otherlv_17, grammarAccess.getDictionaryAccess().getEqualsSignKeyword_0_5_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:292:1: ( (lv_domain_18_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:293:1: (lv_domain_18_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:293:1: (lv_domain_18_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:294:3: lv_domain_18_0= RULE_STRING
            	    {
            	    lv_domain_18_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleDictionary707); 

            	    			newLeafNode(lv_domain_18_0, grammarAccess.getDictionaryAccess().getDomainSTRINGTerminalRuleCall_0_5_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getDictionaryRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"domain",
            	            		lv_domain_18_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getDictionaryAccess().getUnorderedGroup_0());
            	    	 				

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getDictionaryAccess().getUnorderedGroup_0()) ) {
                throw new FailedPredicateException(input, "ruleDictionary", "getUnorderedGroupHelper().canLeave(grammarAccess.getDictionaryAccess().getUnorderedGroup_0())");
            }

            }


            }

             
            	  getUnorderedGroupHelper().leave(grammarAccess.getDictionaryAccess().getUnorderedGroup_0());
            	

            }

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:325:2: ( (lv_definitions_19_0= ruleDefinition ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==18||LA2_0==24||LA2_0==31||LA2_0==35||(LA2_0>=48 && LA2_0<=54)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:326:1: (lv_definitions_19_0= ruleDefinition )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:326:1: (lv_definitions_19_0= ruleDefinition )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:327:3: lv_definitions_19_0= ruleDefinition
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getDictionaryAccess().getDefinitionsDefinitionParserRuleCall_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleDefinition_in_ruleDictionary780);
            	    lv_definitions_19_0=ruleDefinition();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getDictionaryRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"definitions",
            	            		lv_definitions_19_0, 
            	            		"Definition");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDictionary"


    // $ANTLR start "entryRuleDefinition"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:351:1: entryRuleDefinition returns [EObject current=null] : iv_ruleDefinition= ruleDefinition EOF ;
    public final EObject entryRuleDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDefinition = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:352:2: (iv_ruleDefinition= ruleDefinition EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:353:2: iv_ruleDefinition= ruleDefinition EOF
            {
             newCompositeNode(grammarAccess.getDefinitionRule()); 
            pushFollow(FOLLOW_ruleDefinition_in_entryRuleDefinition817);
            iv_ruleDefinition=ruleDefinition();

            state._fsp--;

             current =iv_ruleDefinition; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDefinition827); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDefinition"


    // $ANTLR start "ruleDefinition"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:360:1: ruleDefinition returns [EObject current=null] : (this_EnumDef_0= ruleEnumDef | this_ParameterDef_1= ruleParameterDef | this_ActivityDef_2= ruleActivityDef | this_ActivityGroupDef_3= ruleActivityGroupDef | this_ObjectDef_4= ruleObjectDef | this_ResourceDef_5= ruleResourceDef ) ;
    public final EObject ruleDefinition() throws RecognitionException {
        EObject current = null;

        EObject this_EnumDef_0 = null;

        EObject this_ParameterDef_1 = null;

        EObject this_ActivityDef_2 = null;

        EObject this_ActivityGroupDef_3 = null;

        EObject this_ObjectDef_4 = null;

        EObject this_ResourceDef_5 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:363:28: ( (this_EnumDef_0= ruleEnumDef | this_ParameterDef_1= ruleParameterDef | this_ActivityDef_2= ruleActivityDef | this_ActivityGroupDef_3= ruleActivityGroupDef | this_ObjectDef_4= ruleObjectDef | this_ResourceDef_5= ruleResourceDef ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:364:1: (this_EnumDef_0= ruleEnumDef | this_ParameterDef_1= ruleParameterDef | this_ActivityDef_2= ruleActivityDef | this_ActivityGroupDef_3= ruleActivityGroupDef | this_ObjectDef_4= ruleObjectDef | this_ResourceDef_5= ruleResourceDef )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:364:1: (this_EnumDef_0= ruleEnumDef | this_ParameterDef_1= ruleParameterDef | this_ActivityDef_2= ruleActivityDef | this_ActivityGroupDef_3= ruleActivityGroupDef | this_ObjectDef_4= ruleObjectDef | this_ResourceDef_5= ruleResourceDef )
            int alt3=6;
            switch ( input.LA(1) ) {
            case 18:
                {
                alt3=1;
                }
                break;
            case 24:
            case 31:
                {
                alt3=2;
                }
                break;
            case 35:
                {
                alt3=3;
                }
                break;
            case 48:
                {
                alt3=4;
                }
                break;
            case 49:
                {
                alt3=5;
                }
                break;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
                {
                alt3=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:365:5: this_EnumDef_0= ruleEnumDef
                    {
                     
                            newCompositeNode(grammarAccess.getDefinitionAccess().getEnumDefParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleEnumDef_in_ruleDefinition874);
                    this_EnumDef_0=ruleEnumDef();

                    state._fsp--;

                     
                            current = this_EnumDef_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:375:5: this_ParameterDef_1= ruleParameterDef
                    {
                     
                            newCompositeNode(grammarAccess.getDefinitionAccess().getParameterDefParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleParameterDef_in_ruleDefinition901);
                    this_ParameterDef_1=ruleParameterDef();

                    state._fsp--;

                     
                            current = this_ParameterDef_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 3 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:385:5: this_ActivityDef_2= ruleActivityDef
                    {
                     
                            newCompositeNode(grammarAccess.getDefinitionAccess().getActivityDefParserRuleCall_2()); 
                        
                    pushFollow(FOLLOW_ruleActivityDef_in_ruleDefinition928);
                    this_ActivityDef_2=ruleActivityDef();

                    state._fsp--;

                     
                            current = this_ActivityDef_2; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 4 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:395:5: this_ActivityGroupDef_3= ruleActivityGroupDef
                    {
                     
                            newCompositeNode(grammarAccess.getDefinitionAccess().getActivityGroupDefParserRuleCall_3()); 
                        
                    pushFollow(FOLLOW_ruleActivityGroupDef_in_ruleDefinition955);
                    this_ActivityGroupDef_3=ruleActivityGroupDef();

                    state._fsp--;

                     
                            current = this_ActivityGroupDef_3; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 5 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:405:5: this_ObjectDef_4= ruleObjectDef
                    {
                     
                            newCompositeNode(grammarAccess.getDefinitionAccess().getObjectDefParserRuleCall_4()); 
                        
                    pushFollow(FOLLOW_ruleObjectDef_in_ruleDefinition982);
                    this_ObjectDef_4=ruleObjectDef();

                    state._fsp--;

                     
                            current = this_ObjectDef_4; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 6 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:415:5: this_ResourceDef_5= ruleResourceDef
                    {
                     
                            newCompositeNode(grammarAccess.getDefinitionAccess().getResourceDefParserRuleCall_5()); 
                        
                    pushFollow(FOLLOW_ruleResourceDef_in_ruleDefinition1009);
                    this_ResourceDef_5=ruleResourceDef();

                    state._fsp--;

                     
                            current = this_ResourceDef_5; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDefinition"


    // $ANTLR start "entryRuleEnumDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:431:1: entryRuleEnumDef returns [EObject current=null] : iv_ruleEnumDef= ruleEnumDef EOF ;
    public final EObject entryRuleEnumDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumDef = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:432:2: (iv_ruleEnumDef= ruleEnumDef EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:433:2: iv_ruleEnumDef= ruleEnumDef EOF
            {
             newCompositeNode(grammarAccess.getEnumDefRule()); 
            pushFollow(FOLLOW_ruleEnumDef_in_entryRuleEnumDef1044);
            iv_ruleEnumDef=ruleEnumDef();

            state._fsp--;

             current =iv_ruleEnumDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleEnumDef1054); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEnumDef"


    // $ANTLR start "ruleEnumDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:440:1: ruleEnumDef returns [EObject current=null] : (otherlv_0= 'Enum' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_values_3_0= ruleEnumValue ) )* otherlv_4= '}' ) ;
    public final EObject ruleEnumDef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_values_3_0 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:443:28: ( (otherlv_0= 'Enum' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_values_3_0= ruleEnumValue ) )* otherlv_4= '}' ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:444:1: (otherlv_0= 'Enum' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_values_3_0= ruleEnumValue ) )* otherlv_4= '}' )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:444:1: (otherlv_0= 'Enum' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_values_3_0= ruleEnumValue ) )* otherlv_4= '}' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:444:3: otherlv_0= 'Enum' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_values_3_0= ruleEnumValue ) )* otherlv_4= '}'
            {
            otherlv_0=(Token)match(input,18,FOLLOW_18_in_ruleEnumDef1091); 

                	newLeafNode(otherlv_0, grammarAccess.getEnumDefAccess().getEnumKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:448:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:449:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:449:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:450:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleEnumDef1108); 

            			newLeafNode(lv_name_1_0, grammarAccess.getEnumDefAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getEnumDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleEnumDef1125); 

                	newLeafNode(otherlv_2, grammarAccess.getEnumDefAccess().getLeftCurlyBracketKeyword_2());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:470:1: ( (lv_values_3_0= ruleEnumValue ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==21) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:471:1: (lv_values_3_0= ruleEnumValue )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:471:1: (lv_values_3_0= ruleEnumValue )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:472:3: lv_values_3_0= ruleEnumValue
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getEnumDefAccess().getValuesEnumValueParserRuleCall_3_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleEnumValue_in_ruleEnumDef1146);
            	    lv_values_3_0=ruleEnumValue();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getEnumDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"values",
            	            		lv_values_3_0, 
            	            		"EnumValue");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            otherlv_4=(Token)match(input,20,FOLLOW_20_in_ruleEnumDef1159); 

                	newLeafNode(otherlv_4, grammarAccess.getEnumDefAccess().getRightCurlyBracketKeyword_4());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEnumDef"


    // $ANTLR start "entryRuleEnumValue"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:500:1: entryRuleEnumValue returns [EObject current=null] : iv_ruleEnumValue= ruleEnumValue EOF ;
    public final EObject entryRuleEnumValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumValue = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:501:2: (iv_ruleEnumValue= ruleEnumValue EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:502:2: iv_ruleEnumValue= ruleEnumValue EOF
            {
             newCompositeNode(grammarAccess.getEnumValueRule()); 
            pushFollow(FOLLOW_ruleEnumValue_in_entryRuleEnumValue1195);
            iv_ruleEnumValue=ruleEnumValue();

            state._fsp--;

             current =iv_ruleEnumValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleEnumValue1205); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEnumValue"


    // $ANTLR start "ruleEnumValue"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:509:1: ruleEnumValue returns [EObject current=null] : (otherlv_0= 'Literal' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ) ;
    public final EObject ruleEnumValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_name_4_0=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token lv_color_7_0=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_literal_10_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:512:28: ( (otherlv_0= 'Literal' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:513:1: (otherlv_0= 'Literal' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:513:1: (otherlv_0= 'Literal' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:513:3: otherlv_0= 'Literal' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) )
            {
            otherlv_0=(Token)match(input,21,FOLLOW_21_in_ruleEnumValue1242); 

                	newLeafNode(otherlv_0, grammarAccess.getEnumValueAccess().getLiteralKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:517:1: ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:519:1: ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:519:1: ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:520:2: ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?)
            {
             
            	  getUnorderedGroupHelper().enter(grammarAccess.getEnumValueAccess().getUnorderedGroup_1());
            	
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:523:2: ( ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?)
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:524:3: ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+ {...}?
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:524:3: ( ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) ) )+
            int cnt5=0;
            loop5:
            do {
                int alt5=4;
                int LA5_0 = input.LA(1);

                if ( LA5_0 ==11 && getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 0) ) {
                    alt5=1;
                }
                else if ( LA5_0 ==22 && getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 1) ) {
                    alt5=2;
                }
                else if ( LA5_0 ==23 && getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 2) ) {
                    alt5=3;
                }


                switch (alt5) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:526:4: ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:526:4: ({...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:527:5: {...}? => ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleEnumValue", "getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 0)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:527:106: ( ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:528:6: ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 0);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:531:6: ({...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:531:7: {...}? => (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleEnumValue", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:531:16: (otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:531:18: otherlv_2= 'name' otherlv_3= '=' ( (lv_name_4_0= RULE_STRING ) )
            	    {
            	    otherlv_2=(Token)match(input,11,FOLLOW_11_in_ruleEnumValue1300); 

            	        	newLeafNode(otherlv_2, grammarAccess.getEnumValueAccess().getNameKeyword_1_0_0());
            	        
            	    otherlv_3=(Token)match(input,12,FOLLOW_12_in_ruleEnumValue1312); 

            	        	newLeafNode(otherlv_3, grammarAccess.getEnumValueAccess().getEqualsSignKeyword_1_0_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:539:1: ( (lv_name_4_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:540:1: (lv_name_4_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:540:1: (lv_name_4_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:541:3: lv_name_4_0= RULE_STRING
            	    {
            	    lv_name_4_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleEnumValue1329); 

            	    			newLeafNode(lv_name_4_0, grammarAccess.getEnumValueAccess().getNameSTRINGTerminalRuleCall_1_0_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getEnumValueRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"name",
            	            		lv_name_4_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getEnumValueAccess().getUnorderedGroup_1());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:564:4: ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:564:4: ({...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:565:5: {...}? => ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleEnumValue", "getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 1)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:565:106: ( ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:566:6: ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 1);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:569:6: ({...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:569:7: {...}? => (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleEnumValue", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:569:16: (otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:569:18: otherlv_5= 'color' otherlv_6= '=' ( (lv_color_7_0= RULE_STRING ) )
            	    {
            	    otherlv_5=(Token)match(input,22,FOLLOW_22_in_ruleEnumValue1402); 

            	        	newLeafNode(otherlv_5, grammarAccess.getEnumValueAccess().getColorKeyword_1_1_0());
            	        
            	    otherlv_6=(Token)match(input,12,FOLLOW_12_in_ruleEnumValue1414); 

            	        	newLeafNode(otherlv_6, grammarAccess.getEnumValueAccess().getEqualsSignKeyword_1_1_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:577:1: ( (lv_color_7_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:578:1: (lv_color_7_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:578:1: (lv_color_7_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:579:3: lv_color_7_0= RULE_STRING
            	    {
            	    lv_color_7_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleEnumValue1431); 

            	    			newLeafNode(lv_color_7_0, grammarAccess.getEnumValueAccess().getColorSTRINGTerminalRuleCall_1_1_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getEnumValueRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"color",
            	            		lv_color_7_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getEnumValueAccess().getUnorderedGroup_1());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 3 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:602:4: ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:602:4: ({...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:603:5: {...}? => ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 2) ) {
            	        throw new FailedPredicateException(input, "ruleEnumValue", "getUnorderedGroupHelper().canSelect(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 2)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:603:106: ( ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:604:6: ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getEnumValueAccess().getUnorderedGroup_1(), 2);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:607:6: ({...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:607:7: {...}? => (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleEnumValue", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:607:16: (otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:607:18: otherlv_8= 'literal' otherlv_9= '=' ( (lv_literal_10_0= RULE_STRING ) )
            	    {
            	    otherlv_8=(Token)match(input,23,FOLLOW_23_in_ruleEnumValue1504); 

            	        	newLeafNode(otherlv_8, grammarAccess.getEnumValueAccess().getLiteralKeyword_1_2_0());
            	        
            	    otherlv_9=(Token)match(input,12,FOLLOW_12_in_ruleEnumValue1516); 

            	        	newLeafNode(otherlv_9, grammarAccess.getEnumValueAccess().getEqualsSignKeyword_1_2_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:615:1: ( (lv_literal_10_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:616:1: (lv_literal_10_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:616:1: (lv_literal_10_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:617:3: lv_literal_10_0= RULE_STRING
            	    {
            	    lv_literal_10_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleEnumValue1533); 

            	    			newLeafNode(lv_literal_10_0, grammarAccess.getEnumValueAccess().getLiteralSTRINGTerminalRuleCall_1_2_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getEnumValueRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"literal",
            	            		lv_literal_10_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getEnumValueAccess().getUnorderedGroup_1());
            	    	 				

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

            if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getEnumValueAccess().getUnorderedGroup_1()) ) {
                throw new FailedPredicateException(input, "ruleEnumValue", "getUnorderedGroupHelper().canLeave(grammarAccess.getEnumValueAccess().getUnorderedGroup_1())");
            }

            }


            }

             
            	  getUnorderedGroupHelper().leave(grammarAccess.getEnumValueAccess().getUnorderedGroup_1());
            	

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEnumValue"


    // $ANTLR start "entryRuleParameterDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:656:1: entryRuleParameterDef returns [EObject current=null] : iv_ruleParameterDef= ruleParameterDef EOF ;
    public final EObject entryRuleParameterDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameterDef = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:657:2: (iv_ruleParameterDef= ruleParameterDef EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:658:2: iv_ruleParameterDef= ruleParameterDef EOF
            {
             newCompositeNode(grammarAccess.getParameterDefRule()); 
            pushFollow(FOLLOW_ruleParameterDef_in_entryRuleParameterDef1621);
            iv_ruleParameterDef=ruleParameterDef();

            state._fsp--;

             current =iv_ruleParameterDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameterDef1631); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParameterDef"


    // $ANTLR start "ruleParameterDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:665:1: ruleParameterDef returns [EObject current=null] : (this_AttributeDef_0= ruleAttributeDef | this_ReferenceDef_1= ruleReferenceDef ) ;
    public final EObject ruleParameterDef() throws RecognitionException {
        EObject current = null;

        EObject this_AttributeDef_0 = null;

        EObject this_ReferenceDef_1 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:668:28: ( (this_AttributeDef_0= ruleAttributeDef | this_ReferenceDef_1= ruleReferenceDef ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:669:1: (this_AttributeDef_0= ruleAttributeDef | this_ReferenceDef_1= ruleReferenceDef )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:669:1: (this_AttributeDef_0= ruleAttributeDef | this_ReferenceDef_1= ruleReferenceDef )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==24) ) {
                alt6=1;
            }
            else if ( (LA6_0==31) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:670:5: this_AttributeDef_0= ruleAttributeDef
                    {
                     
                            newCompositeNode(grammarAccess.getParameterDefAccess().getAttributeDefParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleAttributeDef_in_ruleParameterDef1678);
                    this_AttributeDef_0=ruleAttributeDef();

                    state._fsp--;

                     
                            current = this_AttributeDef_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:680:5: this_ReferenceDef_1= ruleReferenceDef
                    {
                     
                            newCompositeNode(grammarAccess.getParameterDefAccess().getReferenceDefParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleReferenceDef_in_ruleParameterDef1705);
                    this_ReferenceDef_1=ruleReferenceDef();

                    state._fsp--;

                     
                            current = this_ReferenceDef_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParameterDef"


    // $ANTLR start "entryRuleAttributeDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:696:1: entryRuleAttributeDef returns [EObject current=null] : iv_ruleAttributeDef= ruleAttributeDef EOF ;
    public final EObject entryRuleAttributeDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDef = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:697:2: (iv_ruleAttributeDef= ruleAttributeDef EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:698:2: iv_ruleAttributeDef= ruleAttributeDef EOF
            {
             newCompositeNode(grammarAccess.getAttributeDefRule()); 
            pushFollow(FOLLOW_ruleAttributeDef_in_entryRuleAttributeDef1740);
            iv_ruleAttributeDef=ruleAttributeDef();

            state._fsp--;

             current =iv_ruleAttributeDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAttributeDef1750); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttributeDef"


    // $ANTLR start "ruleAttributeDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:705:1: ruleAttributeDef returns [EObject current=null] : (otherlv_0= 'attribute' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_26_0= ruleAnnotation ) )* otherlv_27= '}' ) ;
    public final EObject ruleAttributeDef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token lv_type_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token lv_defaultValueLiteral_7_0=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_description_10_0=null;
        Token otherlv_11=null;
        Token otherlv_12=null;
        Token lv_shortDescription_13_0=null;
        Token otherlv_14=null;
        Token otherlv_15=null;
        Token lv_units_16_0=null;
        Token otherlv_17=null;
        Token otherlv_18=null;
        Token lv_displayName_19_0=null;
        Token otherlv_20=null;
        Token otherlv_21=null;
        Token lv_category_22_0=null;
        Token otherlv_23=null;
        Token otherlv_24=null;
        Token lv_parameterName_25_0=null;
        Token otherlv_27=null;
        EObject lv_annotations_26_0 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:708:28: ( (otherlv_0= 'attribute' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_26_0= ruleAnnotation ) )* otherlv_27= '}' ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:709:1: (otherlv_0= 'attribute' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_26_0= ruleAnnotation ) )* otherlv_27= '}' )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:709:1: (otherlv_0= 'attribute' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_26_0= ruleAnnotation ) )* otherlv_27= '}' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:709:3: otherlv_0= 'attribute' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_26_0= ruleAnnotation ) )* otherlv_27= '}'
            {
            otherlv_0=(Token)match(input,24,FOLLOW_24_in_ruleAttributeDef1787); 

                	newLeafNode(otherlv_0, grammarAccess.getAttributeDefAccess().getAttributeKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:713:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:714:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:714:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:715:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAttributeDef1804); 

            			newLeafNode(lv_name_1_0, grammarAccess.getAttributeDefAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:731:2: ( (lv_type_2_0= RULE_STRING ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:732:1: (lv_type_2_0= RULE_STRING )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:732:1: (lv_type_2_0= RULE_STRING )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:733:3: lv_type_2_0= RULE_STRING
            {
            lv_type_2_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAttributeDef1826); 

            			newLeafNode(lv_type_2_0, grammarAccess.getAttributeDefAccess().getTypeSTRINGTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"type",
                    		lv_type_2_0, 
                    		"STRING");
            	    

            }


            }

            otherlv_3=(Token)match(input,19,FOLLOW_19_in_ruleAttributeDef1843); 

                	newLeafNode(otherlv_3, grammarAccess.getAttributeDefAccess().getLeftCurlyBracketKeyword_3());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:753:1: ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:755:1: ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:755:1: ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:756:2: ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* )
            {
             
            	  getUnorderedGroupHelper().enter(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:759:2: ( ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )* )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:760:3: ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )*
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:760:3: ( ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) ) )*
            loop7:
            do {
                int alt7=8;
                int LA7_0 = input.LA(1);

                if ( LA7_0 ==25 && getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 0) ) {
                    alt7=1;
                }
                else if ( LA7_0 ==15 && getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 1) ) {
                    alt7=2;
                }
                else if ( LA7_0 ==26 && getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 2) ) {
                    alt7=3;
                }
                else if ( LA7_0 ==27 && getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 3) ) {
                    alt7=4;
                }
                else if ( LA7_0 ==28 && getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 4) ) {
                    alt7=5;
                }
                else if ( LA7_0 ==29 && getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 5) ) {
                    alt7=6;
                }
                else if ( LA7_0 ==30 && getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 6) ) {
                    alt7=7;
                }


                switch (alt7) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:762:4: ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:762:4: ({...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:763:5: {...}? => ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 0)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:763:109: ( ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:764:6: ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 0);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:767:6: ({...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:767:7: {...}? => (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:767:16: (otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:767:18: otherlv_5= 'defaultValueLiteral' otherlv_6= '=' ( (lv_defaultValueLiteral_7_0= RULE_STRING ) )
            	    {
            	    otherlv_5=(Token)match(input,25,FOLLOW_25_in_ruleAttributeDef1901); 

            	        	newLeafNode(otherlv_5, grammarAccess.getAttributeDefAccess().getDefaultValueLiteralKeyword_4_0_0());
            	        
            	    otherlv_6=(Token)match(input,12,FOLLOW_12_in_ruleAttributeDef1913); 

            	        	newLeafNode(otherlv_6, grammarAccess.getAttributeDefAccess().getEqualsSignKeyword_4_0_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:775:1: ( (lv_defaultValueLiteral_7_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:776:1: (lv_defaultValueLiteral_7_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:776:1: (lv_defaultValueLiteral_7_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:777:3: lv_defaultValueLiteral_7_0= RULE_STRING
            	    {
            	    lv_defaultValueLiteral_7_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAttributeDef1930); 

            	    			newLeafNode(lv_defaultValueLiteral_7_0, grammarAccess.getAttributeDefAccess().getDefaultValueLiteralSTRINGTerminalRuleCall_4_0_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"defaultValueLiteral",
            	            		lv_defaultValueLiteral_7_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:800:4: ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:800:4: ({...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:801:5: {...}? => ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 1)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:801:109: ( ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:802:6: ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 1);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:805:6: ({...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:805:7: {...}? => (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:805:16: (otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:805:18: otherlv_8= 'description' otherlv_9= '=' ( (lv_description_10_0= RULE_STRING ) )
            	    {
            	    otherlv_8=(Token)match(input,15,FOLLOW_15_in_ruleAttributeDef2003); 

            	        	newLeafNode(otherlv_8, grammarAccess.getAttributeDefAccess().getDescriptionKeyword_4_1_0());
            	        
            	    otherlv_9=(Token)match(input,12,FOLLOW_12_in_ruleAttributeDef2015); 

            	        	newLeafNode(otherlv_9, grammarAccess.getAttributeDefAccess().getEqualsSignKeyword_4_1_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:813:1: ( (lv_description_10_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:814:1: (lv_description_10_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:814:1: (lv_description_10_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:815:3: lv_description_10_0= RULE_STRING
            	    {
            	    lv_description_10_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAttributeDef2032); 

            	    			newLeafNode(lv_description_10_0, grammarAccess.getAttributeDefAccess().getDescriptionSTRINGTerminalRuleCall_4_1_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"description",
            	            		lv_description_10_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 3 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:838:4: ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:838:4: ({...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:839:5: {...}? => ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 2) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 2)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:839:109: ( ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:840:6: ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 2);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:843:6: ({...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:843:7: {...}? => (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:843:16: (otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:843:18: otherlv_11= 'shortDescription' otherlv_12= '=' ( (lv_shortDescription_13_0= RULE_STRING ) )
            	    {
            	    otherlv_11=(Token)match(input,26,FOLLOW_26_in_ruleAttributeDef2105); 

            	        	newLeafNode(otherlv_11, grammarAccess.getAttributeDefAccess().getShortDescriptionKeyword_4_2_0());
            	        
            	    otherlv_12=(Token)match(input,12,FOLLOW_12_in_ruleAttributeDef2117); 

            	        	newLeafNode(otherlv_12, grammarAccess.getAttributeDefAccess().getEqualsSignKeyword_4_2_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:851:1: ( (lv_shortDescription_13_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:852:1: (lv_shortDescription_13_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:852:1: (lv_shortDescription_13_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:853:3: lv_shortDescription_13_0= RULE_STRING
            	    {
            	    lv_shortDescription_13_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAttributeDef2134); 

            	    			newLeafNode(lv_shortDescription_13_0, grammarAccess.getAttributeDefAccess().getShortDescriptionSTRINGTerminalRuleCall_4_2_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"shortDescription",
            	            		lv_shortDescription_13_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 4 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:876:4: ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:876:4: ({...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:877:5: {...}? => ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 3) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 3)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:877:109: ( ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:878:6: ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 3);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:881:6: ({...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:881:7: {...}? => (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:881:16: (otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:881:18: otherlv_14= 'units' otherlv_15= '=' ( (lv_units_16_0= RULE_STRING ) )
            	    {
            	    otherlv_14=(Token)match(input,27,FOLLOW_27_in_ruleAttributeDef2207); 

            	        	newLeafNode(otherlv_14, grammarAccess.getAttributeDefAccess().getUnitsKeyword_4_3_0());
            	        
            	    otherlv_15=(Token)match(input,12,FOLLOW_12_in_ruleAttributeDef2219); 

            	        	newLeafNode(otherlv_15, grammarAccess.getAttributeDefAccess().getEqualsSignKeyword_4_3_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:889:1: ( (lv_units_16_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:890:1: (lv_units_16_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:890:1: (lv_units_16_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:891:3: lv_units_16_0= RULE_STRING
            	    {
            	    lv_units_16_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAttributeDef2236); 

            	    			newLeafNode(lv_units_16_0, grammarAccess.getAttributeDefAccess().getUnitsSTRINGTerminalRuleCall_4_3_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"units",
            	            		lv_units_16_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 5 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:914:4: ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:914:4: ({...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:915:5: {...}? => ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 4) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 4)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:915:109: ( ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:916:6: ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 4);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:919:6: ({...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:919:7: {...}? => (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:919:16: (otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:919:18: otherlv_17= 'displayName' otherlv_18= '=' ( (lv_displayName_19_0= RULE_STRING ) )
            	    {
            	    otherlv_17=(Token)match(input,28,FOLLOW_28_in_ruleAttributeDef2309); 

            	        	newLeafNode(otherlv_17, grammarAccess.getAttributeDefAccess().getDisplayNameKeyword_4_4_0());
            	        
            	    otherlv_18=(Token)match(input,12,FOLLOW_12_in_ruleAttributeDef2321); 

            	        	newLeafNode(otherlv_18, grammarAccess.getAttributeDefAccess().getEqualsSignKeyword_4_4_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:927:1: ( (lv_displayName_19_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:928:1: (lv_displayName_19_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:928:1: (lv_displayName_19_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:929:3: lv_displayName_19_0= RULE_STRING
            	    {
            	    lv_displayName_19_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAttributeDef2338); 

            	    			newLeafNode(lv_displayName_19_0, grammarAccess.getAttributeDefAccess().getDisplayNameSTRINGTerminalRuleCall_4_4_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"displayName",
            	            		lv_displayName_19_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 6 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:952:4: ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:952:4: ({...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:953:5: {...}? => ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 5) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 5)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:953:109: ( ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:954:6: ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 5);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:957:6: ({...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:957:7: {...}? => (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:957:16: (otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:957:18: otherlv_20= 'category' otherlv_21= '=' ( (lv_category_22_0= RULE_STRING ) )
            	    {
            	    otherlv_20=(Token)match(input,29,FOLLOW_29_in_ruleAttributeDef2411); 

            	        	newLeafNode(otherlv_20, grammarAccess.getAttributeDefAccess().getCategoryKeyword_4_5_0());
            	        
            	    otherlv_21=(Token)match(input,12,FOLLOW_12_in_ruleAttributeDef2423); 

            	        	newLeafNode(otherlv_21, grammarAccess.getAttributeDefAccess().getEqualsSignKeyword_4_5_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:965:1: ( (lv_category_22_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:966:1: (lv_category_22_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:966:1: (lv_category_22_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:967:3: lv_category_22_0= RULE_STRING
            	    {
            	    lv_category_22_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAttributeDef2440); 

            	    			newLeafNode(lv_category_22_0, grammarAccess.getAttributeDefAccess().getCategorySTRINGTerminalRuleCall_4_5_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"category",
            	            		lv_category_22_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 7 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:990:4: ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:990:4: ({...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:991:5: {...}? => ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 6) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 6)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:991:109: ( ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:992:6: ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4(), 6);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:995:6: ({...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:995:7: {...}? => (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleAttributeDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:995:16: (otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:995:18: otherlv_23= 'parameterName' otherlv_24= '=' ( (lv_parameterName_25_0= RULE_STRING ) )
            	    {
            	    otherlv_23=(Token)match(input,30,FOLLOW_30_in_ruleAttributeDef2513); 

            	        	newLeafNode(otherlv_23, grammarAccess.getAttributeDefAccess().getParameterNameKeyword_4_6_0());
            	        
            	    otherlv_24=(Token)match(input,12,FOLLOW_12_in_ruleAttributeDef2525); 

            	        	newLeafNode(otherlv_24, grammarAccess.getAttributeDefAccess().getEqualsSignKeyword_4_6_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1003:1: ( (lv_parameterName_25_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1004:1: (lv_parameterName_25_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1004:1: (lv_parameterName_25_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1005:3: lv_parameterName_25_0= RULE_STRING
            	    {
            	    lv_parameterName_25_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAttributeDef2542); 

            	    			newLeafNode(lv_parameterName_25_0, grammarAccess.getAttributeDefAccess().getParameterNameSTRINGTerminalRuleCall_4_6_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getAttributeDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"parameterName",
            	            		lv_parameterName_25_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }


            }

             
            	  getUnorderedGroupHelper().leave(grammarAccess.getAttributeDefAccess().getUnorderedGroup_4());
            	

            }

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1035:2: ( (lv_annotations_26_0= ruleAnnotation ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==38) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1036:1: (lv_annotations_26_0= ruleAnnotation )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1036:1: (lv_annotations_26_0= ruleAnnotation )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1037:3: lv_annotations_26_0= ruleAnnotation
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getAttributeDefAccess().getAnnotationsAnnotationParserRuleCall_5_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleAnnotation_in_ruleAttributeDef2609);
            	    lv_annotations_26_0=ruleAnnotation();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getAttributeDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"annotations",
            	            		lv_annotations_26_0, 
            	            		"Annotation");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            otherlv_27=(Token)match(input,20,FOLLOW_20_in_ruleAttributeDef2622); 

                	newLeafNode(otherlv_27, grammarAccess.getAttributeDefAccess().getRightCurlyBracketKeyword_6());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttributeDef"


    // $ANTLR start "entryRuleReferenceDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1065:1: entryRuleReferenceDef returns [EObject current=null] : iv_ruleReferenceDef= ruleReferenceDef EOF ;
    public final EObject entryRuleReferenceDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleReferenceDef = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1066:2: (iv_ruleReferenceDef= ruleReferenceDef EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1067:2: iv_ruleReferenceDef= ruleReferenceDef EOF
            {
             newCompositeNode(grammarAccess.getReferenceDefRule()); 
            pushFollow(FOLLOW_ruleReferenceDef_in_entryRuleReferenceDef2658);
            iv_ruleReferenceDef=ruleReferenceDef();

            state._fsp--;

             current =iv_ruleReferenceDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleReferenceDef2668); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleReferenceDef"


    // $ANTLR start "ruleReferenceDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1074:1: ruleReferenceDef returns [EObject current=null] : (otherlv_0= 'reference' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_17_0= ruleAnnotation ) )* ( (lv_requirements_18_0= ruleRequirement ) )* ( (lv_effects_19_0= ruleEffect ) )* otherlv_20= '}' ) ;
    public final EObject ruleReferenceDef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token lv_type_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token lv_description_7_0=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_displayName_10_0=null;
        Token otherlv_11=null;
        Token otherlv_12=null;
        Token lv_category_13_0=null;
        Token otherlv_14=null;
        Token otherlv_15=null;
        Token otherlv_20=null;
        AntlrDatatypeRuleToken lv_containment_16_0 = null;

        EObject lv_annotations_17_0 = null;

        EObject lv_requirements_18_0 = null;

        AntlrDatatypeRuleToken lv_effects_19_0 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1077:28: ( (otherlv_0= 'reference' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_17_0= ruleAnnotation ) )* ( (lv_requirements_18_0= ruleRequirement ) )* ( (lv_effects_19_0= ruleEffect ) )* otherlv_20= '}' ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1078:1: (otherlv_0= 'reference' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_17_0= ruleAnnotation ) )* ( (lv_requirements_18_0= ruleRequirement ) )* ( (lv_effects_19_0= ruleEffect ) )* otherlv_20= '}' )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1078:1: (otherlv_0= 'reference' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_17_0= ruleAnnotation ) )* ( (lv_requirements_18_0= ruleRequirement ) )* ( (lv_effects_19_0= ruleEffect ) )* otherlv_20= '}' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1078:3: otherlv_0= 'reference' ( (lv_name_1_0= RULE_ID ) ) ( (lv_type_2_0= RULE_STRING ) ) otherlv_3= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_17_0= ruleAnnotation ) )* ( (lv_requirements_18_0= ruleRequirement ) )* ( (lv_effects_19_0= ruleEffect ) )* otherlv_20= '}'
            {
            otherlv_0=(Token)match(input,31,FOLLOW_31_in_ruleReferenceDef2705); 

                	newLeafNode(otherlv_0, grammarAccess.getReferenceDefAccess().getReferenceKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1082:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1083:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1083:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1084:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleReferenceDef2722); 

            			newLeafNode(lv_name_1_0, grammarAccess.getReferenceDefAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getReferenceDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1100:2: ( (lv_type_2_0= RULE_STRING ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1101:1: (lv_type_2_0= RULE_STRING )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1101:1: (lv_type_2_0= RULE_STRING )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1102:3: lv_type_2_0= RULE_STRING
            {
            lv_type_2_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleReferenceDef2744); 

            			newLeafNode(lv_type_2_0, grammarAccess.getReferenceDefAccess().getTypeSTRINGTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getReferenceDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"type",
                    		lv_type_2_0, 
                    		"STRING");
            	    

            }


            }

            otherlv_3=(Token)match(input,19,FOLLOW_19_in_ruleReferenceDef2761); 

                	newLeafNode(otherlv_3, grammarAccess.getReferenceDefAccess().getLeftCurlyBracketKeyword_3());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1122:1: ( ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1124:1: ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1124:1: ( ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1125:2: ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* )
            {
             
            	  getUnorderedGroupHelper().enter(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4());
            	
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1128:2: ( ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )* )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1129:3: ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )*
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1129:3: ( ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) ) )*
            loop9:
            do {
                int alt9=5;
                int LA9_0 = input.LA(1);

                if ( LA9_0 ==15 && getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 0) ) {
                    alt9=1;
                }
                else if ( LA9_0 ==28 && getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 1) ) {
                    alt9=2;
                }
                else if ( LA9_0 ==29 && getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 2) ) {
                    alt9=3;
                }
                else if ( LA9_0 ==32 && getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 3) ) {
                    alt9=4;
                }


                switch (alt9) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1131:4: ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1131:4: ({...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1132:5: {...}? => ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleReferenceDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 0)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1132:109: ( ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1133:6: ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 0);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1136:6: ({...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1136:7: {...}? => (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleReferenceDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1136:16: (otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1136:18: otherlv_5= 'description' otherlv_6= '=' ( (lv_description_7_0= RULE_STRING ) )
            	    {
            	    otherlv_5=(Token)match(input,15,FOLLOW_15_in_ruleReferenceDef2819); 

            	        	newLeafNode(otherlv_5, grammarAccess.getReferenceDefAccess().getDescriptionKeyword_4_0_0());
            	        
            	    otherlv_6=(Token)match(input,12,FOLLOW_12_in_ruleReferenceDef2831); 

            	        	newLeafNode(otherlv_6, grammarAccess.getReferenceDefAccess().getEqualsSignKeyword_4_0_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1144:1: ( (lv_description_7_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1145:1: (lv_description_7_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1145:1: (lv_description_7_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1146:3: lv_description_7_0= RULE_STRING
            	    {
            	    lv_description_7_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleReferenceDef2848); 

            	    			newLeafNode(lv_description_7_0, grammarAccess.getReferenceDefAccess().getDescriptionSTRINGTerminalRuleCall_4_0_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getReferenceDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"description",
            	            		lv_description_7_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1169:4: ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1169:4: ({...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1170:5: {...}? => ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleReferenceDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 1)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1170:109: ( ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1171:6: ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 1);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1174:6: ({...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1174:7: {...}? => (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleReferenceDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1174:16: (otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1174:18: otherlv_8= 'displayName' otherlv_9= '=' ( (lv_displayName_10_0= RULE_STRING ) )
            	    {
            	    otherlv_8=(Token)match(input,28,FOLLOW_28_in_ruleReferenceDef2921); 

            	        	newLeafNode(otherlv_8, grammarAccess.getReferenceDefAccess().getDisplayNameKeyword_4_1_0());
            	        
            	    otherlv_9=(Token)match(input,12,FOLLOW_12_in_ruleReferenceDef2933); 

            	        	newLeafNode(otherlv_9, grammarAccess.getReferenceDefAccess().getEqualsSignKeyword_4_1_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1182:1: ( (lv_displayName_10_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1183:1: (lv_displayName_10_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1183:1: (lv_displayName_10_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1184:3: lv_displayName_10_0= RULE_STRING
            	    {
            	    lv_displayName_10_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleReferenceDef2950); 

            	    			newLeafNode(lv_displayName_10_0, grammarAccess.getReferenceDefAccess().getDisplayNameSTRINGTerminalRuleCall_4_1_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getReferenceDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"displayName",
            	            		lv_displayName_10_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 3 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1207:4: ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1207:4: ({...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1208:5: {...}? => ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 2) ) {
            	        throw new FailedPredicateException(input, "ruleReferenceDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 2)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1208:109: ( ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1209:6: ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 2);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1212:6: ({...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1212:7: {...}? => (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleReferenceDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1212:16: (otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1212:18: otherlv_11= 'category' otherlv_12= '=' ( (lv_category_13_0= RULE_STRING ) )
            	    {
            	    otherlv_11=(Token)match(input,29,FOLLOW_29_in_ruleReferenceDef3023); 

            	        	newLeafNode(otherlv_11, grammarAccess.getReferenceDefAccess().getCategoryKeyword_4_2_0());
            	        
            	    otherlv_12=(Token)match(input,12,FOLLOW_12_in_ruleReferenceDef3035); 

            	        	newLeafNode(otherlv_12, grammarAccess.getReferenceDefAccess().getEqualsSignKeyword_4_2_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1220:1: ( (lv_category_13_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1221:1: (lv_category_13_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1221:1: (lv_category_13_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1222:3: lv_category_13_0= RULE_STRING
            	    {
            	    lv_category_13_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleReferenceDef3052); 

            	    			newLeafNode(lv_category_13_0, grammarAccess.getReferenceDefAccess().getCategorySTRINGTerminalRuleCall_4_2_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getReferenceDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"category",
            	            		lv_category_13_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 4 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1245:4: ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1245:4: ({...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1246:5: {...}? => ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 3) ) {
            	        throw new FailedPredicateException(input, "ruleReferenceDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 3)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1246:109: ( ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1247:6: ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4(), 3);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1250:6: ({...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1250:7: {...}? => (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleReferenceDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1250:16: (otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1250:18: otherlv_14= 'containment' otherlv_15= '=' ( (lv_containment_16_0= ruleBoolean ) )
            	    {
            	    otherlv_14=(Token)match(input,32,FOLLOW_32_in_ruleReferenceDef3125); 

            	        	newLeafNode(otherlv_14, grammarAccess.getReferenceDefAccess().getContainmentKeyword_4_3_0());
            	        
            	    otherlv_15=(Token)match(input,12,FOLLOW_12_in_ruleReferenceDef3137); 

            	        	newLeafNode(otherlv_15, grammarAccess.getReferenceDefAccess().getEqualsSignKeyword_4_3_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1258:1: ( (lv_containment_16_0= ruleBoolean ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1259:1: (lv_containment_16_0= ruleBoolean )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1259:1: (lv_containment_16_0= ruleBoolean )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1260:3: lv_containment_16_0= ruleBoolean
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getReferenceDefAccess().getContainmentBooleanParserRuleCall_4_3_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleBoolean_in_ruleReferenceDef3158);
            	    lv_containment_16_0=ruleBoolean();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getReferenceDefRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"containment",
            	            		lv_containment_16_0, 
            	            		"Boolean");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4());
            	    	 				

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }


            }

             
            	  getUnorderedGroupHelper().leave(grammarAccess.getReferenceDefAccess().getUnorderedGroup_4());
            	

            }

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1290:2: ( (lv_annotations_17_0= ruleAnnotation ) )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==38) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1291:1: (lv_annotations_17_0= ruleAnnotation )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1291:1: (lv_annotations_17_0= ruleAnnotation )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1292:3: lv_annotations_17_0= ruleAnnotation
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getReferenceDefAccess().getAnnotationsAnnotationParserRuleCall_5_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleAnnotation_in_ruleReferenceDef3220);
            	    lv_annotations_17_0=ruleAnnotation();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getReferenceDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"annotations",
            	            		lv_annotations_17_0, 
            	            		"Annotation");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1308:3: ( (lv_requirements_18_0= ruleRequirement ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==39||LA11_0==41) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1309:1: (lv_requirements_18_0= ruleRequirement )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1309:1: (lv_requirements_18_0= ruleRequirement )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1310:3: lv_requirements_18_0= ruleRequirement
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getReferenceDefAccess().getRequirementsRequirementParserRuleCall_6_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleRequirement_in_ruleReferenceDef3242);
            	    lv_requirements_18_0=ruleRequirement();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getReferenceDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"requirements",
            	            		lv_requirements_18_0, 
            	            		"Requirement");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1326:3: ( (lv_effects_19_0= ruleEffect ) )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=44 && LA12_0<=47)) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1327:1: (lv_effects_19_0= ruleEffect )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1327:1: (lv_effects_19_0= ruleEffect )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1328:3: lv_effects_19_0= ruleEffect
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getReferenceDefAccess().getEffectsEffectParserRuleCall_7_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleEffect_in_ruleReferenceDef3264);
            	    lv_effects_19_0=ruleEffect();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getReferenceDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"effects",
            	            		lv_effects_19_0, 
            	            		"Effect");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            otherlv_20=(Token)match(input,20,FOLLOW_20_in_ruleReferenceDef3277); 

                	newLeafNode(otherlv_20, grammarAccess.getReferenceDefAccess().getRightCurlyBracketKeyword_8());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleReferenceDef"


    // $ANTLR start "entryRuleBoolean"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1356:1: entryRuleBoolean returns [String current=null] : iv_ruleBoolean= ruleBoolean EOF ;
    public final String entryRuleBoolean() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleBoolean = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1357:2: (iv_ruleBoolean= ruleBoolean EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1358:2: iv_ruleBoolean= ruleBoolean EOF
            {
             newCompositeNode(grammarAccess.getBooleanRule()); 
            pushFollow(FOLLOW_ruleBoolean_in_entryRuleBoolean3314);
            iv_ruleBoolean=ruleBoolean();

            state._fsp--;

             current =iv_ruleBoolean.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBoolean3325); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBoolean"


    // $ANTLR start "ruleBoolean"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1365:1: ruleBoolean returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'true' | kw= 'false' ) ;
    public final AntlrDatatypeRuleToken ruleBoolean() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1368:28: ( (kw= 'true' | kw= 'false' ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1369:1: (kw= 'true' | kw= 'false' )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1369:1: (kw= 'true' | kw= 'false' )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==33) ) {
                alt13=1;
            }
            else if ( (LA13_0==34) ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1370:2: kw= 'true'
                    {
                    kw=(Token)match(input,33,FOLLOW_33_in_ruleBoolean3363); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getBooleanAccess().getTrueKeyword_0()); 
                        

                    }
                    break;
                case 2 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1377:2: kw= 'false'
                    {
                    kw=(Token)match(input,34,FOLLOW_34_in_ruleBoolean3382); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getBooleanAccess().getFalseKeyword_1()); 
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBoolean"


    // $ANTLR start "entryRuleActivityDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1390:1: entryRuleActivityDef returns [EObject current=null] : iv_ruleActivityDef= ruleActivityDef EOF ;
    public final EObject entryRuleActivityDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleActivityDef = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1391:2: (iv_ruleActivityDef= ruleActivityDef EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1392:2: iv_ruleActivityDef= ruleActivityDef EOF
            {
             newCompositeNode(grammarAccess.getActivityDefRule()); 
            pushFollow(FOLLOW_ruleActivityDef_in_entryRuleActivityDef3422);
            iv_ruleActivityDef=ruleActivityDef();

            state._fsp--;

             current =iv_ruleActivityDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleActivityDef3432); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleActivityDef"


    // $ANTLR start "ruleActivityDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1399:1: ruleActivityDef returns [EObject current=null] : (otherlv_0= 'ActivityDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_19_0= ruleAnnotation ) )* ( (lv_parameters_20_0= ruleParameterDef ) )* ( (lv_requirements_21_0= ruleRequirement ) )* ( (lv_effects_22_0= ruleEffect ) )* otherlv_23= '}' ) ;
    public final EObject ruleActivityDef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token lv_description_6_0=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token lv_category_9_0=null;
        Token otherlv_10=null;
        Token otherlv_11=null;
        Token lv_duration_12_0=null;
        Token otherlv_13=null;
        Token otherlv_14=null;
        Token lv_displayName_15_0=null;
        Token otherlv_16=null;
        Token otherlv_17=null;
        Token lv_hiddenParams_18_0=null;
        Token otherlv_23=null;
        EObject lv_annotations_19_0 = null;

        EObject lv_parameters_20_0 = null;

        EObject lv_requirements_21_0 = null;

        AntlrDatatypeRuleToken lv_effects_22_0 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1402:28: ( (otherlv_0= 'ActivityDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_19_0= ruleAnnotation ) )* ( (lv_parameters_20_0= ruleParameterDef ) )* ( (lv_requirements_21_0= ruleRequirement ) )* ( (lv_effects_22_0= ruleEffect ) )* otherlv_23= '}' ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1403:1: (otherlv_0= 'ActivityDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_19_0= ruleAnnotation ) )* ( (lv_parameters_20_0= ruleParameterDef ) )* ( (lv_requirements_21_0= ruleRequirement ) )* ( (lv_effects_22_0= ruleEffect ) )* otherlv_23= '}' )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1403:1: (otherlv_0= 'ActivityDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_19_0= ruleAnnotation ) )* ( (lv_parameters_20_0= ruleParameterDef ) )* ( (lv_requirements_21_0= ruleRequirement ) )* ( (lv_effects_22_0= ruleEffect ) )* otherlv_23= '}' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1403:3: otherlv_0= 'ActivityDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( ( ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* ) ) ) ( (lv_annotations_19_0= ruleAnnotation ) )* ( (lv_parameters_20_0= ruleParameterDef ) )* ( (lv_requirements_21_0= ruleRequirement ) )* ( (lv_effects_22_0= ruleEffect ) )* otherlv_23= '}'
            {
            otherlv_0=(Token)match(input,35,FOLLOW_35_in_ruleActivityDef3469); 

                	newLeafNode(otherlv_0, grammarAccess.getActivityDefAccess().getActivityDefKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1407:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1408:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1408:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1409:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleActivityDef3486); 

            			newLeafNode(lv_name_1_0, grammarAccess.getActivityDefAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getActivityDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleActivityDef3503); 

                	newLeafNode(otherlv_2, grammarAccess.getActivityDefAccess().getLeftCurlyBracketKeyword_2());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1429:1: ( ( ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1431:1: ( ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1431:1: ( ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1432:2: ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* )
            {
             
            	  getUnorderedGroupHelper().enter(grammarAccess.getActivityDefAccess().getUnorderedGroup_3());
            	
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1435:2: ( ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )* )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1436:3: ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )*
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1436:3: ( ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) ) )*
            loop14:
            do {
                int alt14=6;
                int LA14_0 = input.LA(1);

                if ( LA14_0 ==15 && getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 0) ) {
                    alt14=1;
                }
                else if ( LA14_0 ==29 && getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 1) ) {
                    alt14=2;
                }
                else if ( LA14_0 ==36 && getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 2) ) {
                    alt14=3;
                }
                else if ( LA14_0 ==28 && getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 3) ) {
                    alt14=4;
                }
                else if ( LA14_0 ==37 && getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 4) ) {
                    alt14=5;
                }


                switch (alt14) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1438:4: ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1438:4: ({...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1439:5: {...}? => ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 0)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1439:108: ( ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1440:6: ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 0);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1443:6: ({...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1443:7: {...}? => (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1443:16: (otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1443:18: otherlv_4= 'description' otherlv_5= '=' ( (lv_description_6_0= RULE_STRING ) )
            	    {
            	    otherlv_4=(Token)match(input,15,FOLLOW_15_in_ruleActivityDef3561); 

            	        	newLeafNode(otherlv_4, grammarAccess.getActivityDefAccess().getDescriptionKeyword_3_0_0());
            	        
            	    otherlv_5=(Token)match(input,12,FOLLOW_12_in_ruleActivityDef3573); 

            	        	newLeafNode(otherlv_5, grammarAccess.getActivityDefAccess().getEqualsSignKeyword_3_0_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1451:1: ( (lv_description_6_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1452:1: (lv_description_6_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1452:1: (lv_description_6_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1453:3: lv_description_6_0= RULE_STRING
            	    {
            	    lv_description_6_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleActivityDef3590); 

            	    			newLeafNode(lv_description_6_0, grammarAccess.getActivityDefAccess().getDescriptionSTRINGTerminalRuleCall_3_0_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getActivityDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"description",
            	            		lv_description_6_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActivityDefAccess().getUnorderedGroup_3());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1476:4: ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1476:4: ({...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1477:5: {...}? => ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 1)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1477:108: ( ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1478:6: ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 1);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1481:6: ({...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1481:7: {...}? => (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1481:16: (otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1481:18: otherlv_7= 'category' otherlv_8= '=' ( (lv_category_9_0= RULE_STRING ) )
            	    {
            	    otherlv_7=(Token)match(input,29,FOLLOW_29_in_ruleActivityDef3663); 

            	        	newLeafNode(otherlv_7, grammarAccess.getActivityDefAccess().getCategoryKeyword_3_1_0());
            	        
            	    otherlv_8=(Token)match(input,12,FOLLOW_12_in_ruleActivityDef3675); 

            	        	newLeafNode(otherlv_8, grammarAccess.getActivityDefAccess().getEqualsSignKeyword_3_1_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1489:1: ( (lv_category_9_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1490:1: (lv_category_9_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1490:1: (lv_category_9_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1491:3: lv_category_9_0= RULE_STRING
            	    {
            	    lv_category_9_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleActivityDef3692); 

            	    			newLeafNode(lv_category_9_0, grammarAccess.getActivityDefAccess().getCategorySTRINGTerminalRuleCall_3_1_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getActivityDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"category",
            	            		lv_category_9_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActivityDefAccess().getUnorderedGroup_3());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 3 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1514:4: ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1514:4: ({...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1515:5: {...}? => ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 2) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 2)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1515:108: ( ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1516:6: ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 2);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1519:6: ({...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1519:7: {...}? => (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1519:16: (otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1519:18: otherlv_10= 'duration' otherlv_11= '=' ( (lv_duration_12_0= RULE_STRING ) )
            	    {
            	    otherlv_10=(Token)match(input,36,FOLLOW_36_in_ruleActivityDef3765); 

            	        	newLeafNode(otherlv_10, grammarAccess.getActivityDefAccess().getDurationKeyword_3_2_0());
            	        
            	    otherlv_11=(Token)match(input,12,FOLLOW_12_in_ruleActivityDef3777); 

            	        	newLeafNode(otherlv_11, grammarAccess.getActivityDefAccess().getEqualsSignKeyword_3_2_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1527:1: ( (lv_duration_12_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1528:1: (lv_duration_12_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1528:1: (lv_duration_12_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1529:3: lv_duration_12_0= RULE_STRING
            	    {
            	    lv_duration_12_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleActivityDef3794); 

            	    			newLeafNode(lv_duration_12_0, grammarAccess.getActivityDefAccess().getDurationSTRINGTerminalRuleCall_3_2_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getActivityDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"duration",
            	            		lv_duration_12_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActivityDefAccess().getUnorderedGroup_3());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 4 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1552:4: ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1552:4: ({...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1553:5: {...}? => ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 3) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 3)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1553:108: ( ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1554:6: ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 3);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1557:6: ({...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1557:7: {...}? => (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1557:16: (otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1557:18: otherlv_13= 'displayName' otherlv_14= '=' ( (lv_displayName_15_0= RULE_STRING ) )
            	    {
            	    otherlv_13=(Token)match(input,28,FOLLOW_28_in_ruleActivityDef3867); 

            	        	newLeafNode(otherlv_13, grammarAccess.getActivityDefAccess().getDisplayNameKeyword_3_3_0());
            	        
            	    otherlv_14=(Token)match(input,12,FOLLOW_12_in_ruleActivityDef3879); 

            	        	newLeafNode(otherlv_14, grammarAccess.getActivityDefAccess().getEqualsSignKeyword_3_3_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1565:1: ( (lv_displayName_15_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1566:1: (lv_displayName_15_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1566:1: (lv_displayName_15_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1567:3: lv_displayName_15_0= RULE_STRING
            	    {
            	    lv_displayName_15_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleActivityDef3896); 

            	    			newLeafNode(lv_displayName_15_0, grammarAccess.getActivityDefAccess().getDisplayNameSTRINGTerminalRuleCall_3_3_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getActivityDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"displayName",
            	            		lv_displayName_15_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActivityDefAccess().getUnorderedGroup_3());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 5 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1590:4: ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1590:4: ({...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1591:5: {...}? => ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 4) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "getUnorderedGroupHelper().canSelect(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 4)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1591:108: ( ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1592:6: ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getActivityDefAccess().getUnorderedGroup_3(), 4);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1595:6: ({...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1595:7: {...}? => (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleActivityDef", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1595:16: (otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1595:18: otherlv_16= 'hidden' otherlv_17= '=' ( (lv_hiddenParams_18_0= RULE_STRING ) )
            	    {
            	    otherlv_16=(Token)match(input,37,FOLLOW_37_in_ruleActivityDef3969); 

            	        	newLeafNode(otherlv_16, grammarAccess.getActivityDefAccess().getHiddenKeyword_3_4_0());
            	        
            	    otherlv_17=(Token)match(input,12,FOLLOW_12_in_ruleActivityDef3981); 

            	        	newLeafNode(otherlv_17, grammarAccess.getActivityDefAccess().getEqualsSignKeyword_3_4_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1603:1: ( (lv_hiddenParams_18_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1604:1: (lv_hiddenParams_18_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1604:1: (lv_hiddenParams_18_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1605:3: lv_hiddenParams_18_0= RULE_STRING
            	    {
            	    lv_hiddenParams_18_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleActivityDef3998); 

            	    			newLeafNode(lv_hiddenParams_18_0, grammarAccess.getActivityDefAccess().getHiddenParamsSTRINGTerminalRuleCall_3_4_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getActivityDefRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"hiddenParams",
            	            		lv_hiddenParams_18_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActivityDefAccess().getUnorderedGroup_3());
            	    	 				

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }


            }

             
            	  getUnorderedGroupHelper().leave(grammarAccess.getActivityDefAccess().getUnorderedGroup_3());
            	

            }

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1635:2: ( (lv_annotations_19_0= ruleAnnotation ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==38) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1636:1: (lv_annotations_19_0= ruleAnnotation )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1636:1: (lv_annotations_19_0= ruleAnnotation )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1637:3: lv_annotations_19_0= ruleAnnotation
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getActivityDefAccess().getAnnotationsAnnotationParserRuleCall_4_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleAnnotation_in_ruleActivityDef4065);
            	    lv_annotations_19_0=ruleAnnotation();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getActivityDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"annotations",
            	            		lv_annotations_19_0, 
            	            		"Annotation");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1653:3: ( (lv_parameters_20_0= ruleParameterDef ) )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==24||LA16_0==31) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1654:1: (lv_parameters_20_0= ruleParameterDef )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1654:1: (lv_parameters_20_0= ruleParameterDef )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1655:3: lv_parameters_20_0= ruleParameterDef
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getActivityDefAccess().getParametersParameterDefParserRuleCall_5_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleParameterDef_in_ruleActivityDef4087);
            	    lv_parameters_20_0=ruleParameterDef();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getActivityDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"parameters",
            	            		lv_parameters_20_0, 
            	            		"ParameterDef");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1671:3: ( (lv_requirements_21_0= ruleRequirement ) )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==39||LA17_0==41) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1672:1: (lv_requirements_21_0= ruleRequirement )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1672:1: (lv_requirements_21_0= ruleRequirement )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1673:3: lv_requirements_21_0= ruleRequirement
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getActivityDefAccess().getRequirementsRequirementParserRuleCall_6_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleRequirement_in_ruleActivityDef4109);
            	    lv_requirements_21_0=ruleRequirement();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getActivityDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"requirements",
            	            		lv_requirements_21_0, 
            	            		"Requirement");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1689:3: ( (lv_effects_22_0= ruleEffect ) )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>=44 && LA18_0<=47)) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1690:1: (lv_effects_22_0= ruleEffect )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1690:1: (lv_effects_22_0= ruleEffect )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1691:3: lv_effects_22_0= ruleEffect
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getActivityDefAccess().getEffectsEffectParserRuleCall_7_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleEffect_in_ruleActivityDef4131);
            	    lv_effects_22_0=ruleEffect();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getActivityDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"effects",
            	            		lv_effects_22_0, 
            	            		"Effect");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            otherlv_23=(Token)match(input,20,FOLLOW_20_in_ruleActivityDef4144); 

                	newLeafNode(otherlv_23, grammarAccess.getActivityDefAccess().getRightCurlyBracketKeyword_8());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleActivityDef"


    // $ANTLR start "entryRuleAnnotation"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1719:1: entryRuleAnnotation returns [EObject current=null] : iv_ruleAnnotation= ruleAnnotation EOF ;
    public final EObject entryRuleAnnotation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnnotation = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1720:2: (iv_ruleAnnotation= ruleAnnotation EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1721:2: iv_ruleAnnotation= ruleAnnotation EOF
            {
             newCompositeNode(grammarAccess.getAnnotationRule()); 
            pushFollow(FOLLOW_ruleAnnotation_in_entryRuleAnnotation4180);
            iv_ruleAnnotation=ruleAnnotation();

            state._fsp--;

             current =iv_ruleAnnotation; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAnnotation4190); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAnnotation"


    // $ANTLR start "ruleAnnotation"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1728:1: ruleAnnotation returns [EObject current=null] : (otherlv_0= 'annotation' ( (lv_source_1_0= RULE_STRING ) ) ( (lv_key_2_0= RULE_STRING ) ) ( (lv_value_3_0= RULE_STRING ) ) ) ;
    public final EObject ruleAnnotation() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_source_1_0=null;
        Token lv_key_2_0=null;
        Token lv_value_3_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1731:28: ( (otherlv_0= 'annotation' ( (lv_source_1_0= RULE_STRING ) ) ( (lv_key_2_0= RULE_STRING ) ) ( (lv_value_3_0= RULE_STRING ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1732:1: (otherlv_0= 'annotation' ( (lv_source_1_0= RULE_STRING ) ) ( (lv_key_2_0= RULE_STRING ) ) ( (lv_value_3_0= RULE_STRING ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1732:1: (otherlv_0= 'annotation' ( (lv_source_1_0= RULE_STRING ) ) ( (lv_key_2_0= RULE_STRING ) ) ( (lv_value_3_0= RULE_STRING ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1732:3: otherlv_0= 'annotation' ( (lv_source_1_0= RULE_STRING ) ) ( (lv_key_2_0= RULE_STRING ) ) ( (lv_value_3_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,38,FOLLOW_38_in_ruleAnnotation4227); 

                	newLeafNode(otherlv_0, grammarAccess.getAnnotationAccess().getAnnotationKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1736:1: ( (lv_source_1_0= RULE_STRING ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1737:1: (lv_source_1_0= RULE_STRING )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1737:1: (lv_source_1_0= RULE_STRING )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1738:3: lv_source_1_0= RULE_STRING
            {
            lv_source_1_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAnnotation4244); 

            			newLeafNode(lv_source_1_0, grammarAccess.getAnnotationAccess().getSourceSTRINGTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAnnotationRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"source",
                    		lv_source_1_0, 
                    		"STRING");
            	    

            }


            }

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1754:2: ( (lv_key_2_0= RULE_STRING ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1755:1: (lv_key_2_0= RULE_STRING )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1755:1: (lv_key_2_0= RULE_STRING )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1756:3: lv_key_2_0= RULE_STRING
            {
            lv_key_2_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAnnotation4266); 

            			newLeafNode(lv_key_2_0, grammarAccess.getAnnotationAccess().getKeySTRINGTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAnnotationRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"key",
                    		lv_key_2_0, 
                    		"STRING");
            	    

            }


            }

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1772:2: ( (lv_value_3_0= RULE_STRING ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1773:1: (lv_value_3_0= RULE_STRING )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1773:1: (lv_value_3_0= RULE_STRING )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1774:3: lv_value_3_0= RULE_STRING
            {
            lv_value_3_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAnnotation4288); 

            			newLeafNode(lv_value_3_0, grammarAccess.getAnnotationAccess().getValueSTRINGTerminalRuleCall_3_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAnnotationRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"value",
                    		lv_value_3_0, 
                    		"STRING");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAnnotation"


    // $ANTLR start "entryRuleRequirement"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1798:1: entryRuleRequirement returns [EObject current=null] : iv_ruleRequirement= ruleRequirement EOF ;
    public final EObject entryRuleRequirement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRequirement = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1799:2: (iv_ruleRequirement= ruleRequirement EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1800:2: iv_ruleRequirement= ruleRequirement EOF
            {
             newCompositeNode(grammarAccess.getRequirementRule()); 
            pushFollow(FOLLOW_ruleRequirement_in_entryRuleRequirement4329);
            iv_ruleRequirement=ruleRequirement();

            state._fsp--;

             current =iv_ruleRequirement; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleRequirement4339); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRequirement"


    // $ANTLR start "ruleRequirement"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1807:1: ruleRequirement returns [EObject current=null] : (this_NumericRequirement_0= ruleNumericRequirement | this_StateRequirement_1= ruleStateRequirement ) ;
    public final EObject ruleRequirement() throws RecognitionException {
        EObject current = null;

        EObject this_NumericRequirement_0 = null;

        EObject this_StateRequirement_1 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1810:28: ( (this_NumericRequirement_0= ruleNumericRequirement | this_StateRequirement_1= ruleStateRequirement ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1811:1: (this_NumericRequirement_0= ruleNumericRequirement | this_StateRequirement_1= ruleStateRequirement )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1811:1: (this_NumericRequirement_0= ruleNumericRequirement | this_StateRequirement_1= ruleStateRequirement )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==39) ) {
                alt19=1;
            }
            else if ( (LA19_0==41) ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1812:5: this_NumericRequirement_0= ruleNumericRequirement
                    {
                     
                            newCompositeNode(grammarAccess.getRequirementAccess().getNumericRequirementParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleNumericRequirement_in_ruleRequirement4386);
                    this_NumericRequirement_0=ruleNumericRequirement();

                    state._fsp--;

                     
                            current = this_NumericRequirement_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1822:5: this_StateRequirement_1= ruleStateRequirement
                    {
                     
                            newCompositeNode(grammarAccess.getRequirementAccess().getStateRequirementParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleStateRequirement_in_ruleRequirement4413);
                    this_StateRequirement_1=ruleStateRequirement();

                    state._fsp--;

                     
                            current = this_StateRequirement_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRequirement"


    // $ANTLR start "entryRuleNumericRequirement"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1838:1: entryRuleNumericRequirement returns [EObject current=null] : iv_ruleNumericRequirement= ruleNumericRequirement EOF ;
    public final EObject entryRuleNumericRequirement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNumericRequirement = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1839:2: (iv_ruleNumericRequirement= ruleNumericRequirement EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1840:2: iv_ruleNumericRequirement= ruleNumericRequirement EOF
            {
             newCompositeNode(grammarAccess.getNumericRequirementRule()); 
            pushFollow(FOLLOW_ruleNumericRequirement_in_entryRuleNumericRequirement4448);
            iv_ruleNumericRequirement=ruleNumericRequirement();

            state._fsp--;

             current =iv_ruleNumericRequirement; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNumericRequirement4458); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNumericRequirement"


    // $ANTLR start "ruleNumericRequirement"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1847:1: ruleNumericRequirement returns [EObject current=null] : (otherlv_0= 'numericRequirement' otherlv_1= 'expression' otherlv_2= '=' ( (lv_expression_3_0= RULE_STRING ) ) ) ;
    public final EObject ruleNumericRequirement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_expression_3_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1850:28: ( (otherlv_0= 'numericRequirement' otherlv_1= 'expression' otherlv_2= '=' ( (lv_expression_3_0= RULE_STRING ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1851:1: (otherlv_0= 'numericRequirement' otherlv_1= 'expression' otherlv_2= '=' ( (lv_expression_3_0= RULE_STRING ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1851:1: (otherlv_0= 'numericRequirement' otherlv_1= 'expression' otherlv_2= '=' ( (lv_expression_3_0= RULE_STRING ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1851:3: otherlv_0= 'numericRequirement' otherlv_1= 'expression' otherlv_2= '=' ( (lv_expression_3_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,39,FOLLOW_39_in_ruleNumericRequirement4495); 

                	newLeafNode(otherlv_0, grammarAccess.getNumericRequirementAccess().getNumericRequirementKeyword_0());
                
            otherlv_1=(Token)match(input,40,FOLLOW_40_in_ruleNumericRequirement4507); 

                	newLeafNode(otherlv_1, grammarAccess.getNumericRequirementAccess().getExpressionKeyword_1());
                
            otherlv_2=(Token)match(input,12,FOLLOW_12_in_ruleNumericRequirement4519); 

                	newLeafNode(otherlv_2, grammarAccess.getNumericRequirementAccess().getEqualsSignKeyword_2());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1863:1: ( (lv_expression_3_0= RULE_STRING ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1864:1: (lv_expression_3_0= RULE_STRING )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1864:1: (lv_expression_3_0= RULE_STRING )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1865:3: lv_expression_3_0= RULE_STRING
            {
            lv_expression_3_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleNumericRequirement4536); 

            			newLeafNode(lv_expression_3_0, grammarAccess.getNumericRequirementAccess().getExpressionSTRINGTerminalRuleCall_3_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getNumericRequirementRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"expression",
                    		lv_expression_3_0, 
                    		"STRING");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNumericRequirement"


    // $ANTLR start "entryRuleStateRequirement"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1889:1: entryRuleStateRequirement returns [EObject current=null] : iv_ruleStateRequirement= ruleStateRequirement EOF ;
    public final EObject entryRuleStateRequirement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStateRequirement = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1890:2: (iv_ruleStateRequirement= ruleStateRequirement EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1891:2: iv_ruleStateRequirement= ruleStateRequirement EOF
            {
             newCompositeNode(grammarAccess.getStateRequirementRule()); 
            pushFollow(FOLLOW_ruleStateRequirement_in_entryRuleStateRequirement4577);
            iv_ruleStateRequirement=ruleStateRequirement();

            state._fsp--;

             current =iv_ruleStateRequirement; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleStateRequirement4587); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStateRequirement"


    // $ANTLR start "ruleStateRequirement"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1898:1: ruleStateRequirement returns [EObject current=null] : (otherlv_0= 'stateRequirement' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ) ;
    public final EObject ruleStateRequirement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_definition_4_0=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token lv_requiredState_7_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1901:28: ( (otherlv_0= 'stateRequirement' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1902:1: (otherlv_0= 'stateRequirement' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1902:1: (otherlv_0= 'stateRequirement' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1902:3: otherlv_0= 'stateRequirement' ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) )
            {
            otherlv_0=(Token)match(input,41,FOLLOW_41_in_ruleStateRequirement4624); 

                	newLeafNode(otherlv_0, grammarAccess.getStateRequirementAccess().getStateRequirementKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1906:1: ( ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1908:1: ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1908:1: ( ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1909:2: ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?)
            {
             
            	  getUnorderedGroupHelper().enter(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1());
            	
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1912:2: ( ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?)
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1913:3: ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+ {...}?
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1913:3: ( ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) ) | ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) ) )+
            int cnt20=0;
            loop20:
            do {
                int alt20=3;
                int LA20_0 = input.LA(1);

                if ( LA20_0 ==42 && getUnorderedGroupHelper().canSelect(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1(), 0) ) {
                    alt20=1;
                }
                else if ( LA20_0 ==43 && getUnorderedGroupHelper().canSelect(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1(), 1) ) {
                    alt20=2;
                }


                switch (alt20) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1915:4: ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1915:4: ({...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1916:5: {...}? => ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleStateRequirement", "getUnorderedGroupHelper().canSelect(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1(), 0)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1916:113: ( ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1917:6: ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1(), 0);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1920:6: ({...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1920:7: {...}? => (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleStateRequirement", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1920:16: (otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1920:18: otherlv_2= 'definition' otherlv_3= '=' ( (lv_definition_4_0= RULE_STRING ) )
            	    {
            	    otherlv_2=(Token)match(input,42,FOLLOW_42_in_ruleStateRequirement4682); 

            	        	newLeafNode(otherlv_2, grammarAccess.getStateRequirementAccess().getDefinitionKeyword_1_0_0());
            	        
            	    otherlv_3=(Token)match(input,12,FOLLOW_12_in_ruleStateRequirement4694); 

            	        	newLeafNode(otherlv_3, grammarAccess.getStateRequirementAccess().getEqualsSignKeyword_1_0_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1928:1: ( (lv_definition_4_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1929:1: (lv_definition_4_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1929:1: (lv_definition_4_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1930:3: lv_definition_4_0= RULE_STRING
            	    {
            	    lv_definition_4_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleStateRequirement4711); 

            	    			newLeafNode(lv_definition_4_0, grammarAccess.getStateRequirementAccess().getDefinitionSTRINGTerminalRuleCall_1_0_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getStateRequirementRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"definition",
            	            		lv_definition_4_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1953:4: ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1953:4: ({...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1954:5: {...}? => ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleStateRequirement", "getUnorderedGroupHelper().canSelect(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1(), 1)");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1954:113: ( ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1955:6: ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1(), 1);
            	    	 				
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1958:6: ({...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1958:7: {...}? => (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleStateRequirement", "true");
            	    }
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1958:16: (otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1958:18: otherlv_5= 'requiredState' otherlv_6= '=' ( (lv_requiredState_7_0= RULE_STRING ) )
            	    {
            	    otherlv_5=(Token)match(input,43,FOLLOW_43_in_ruleStateRequirement4784); 

            	        	newLeafNode(otherlv_5, grammarAccess.getStateRequirementAccess().getRequiredStateKeyword_1_1_0());
            	        
            	    otherlv_6=(Token)match(input,12,FOLLOW_12_in_ruleStateRequirement4796); 

            	        	newLeafNode(otherlv_6, grammarAccess.getStateRequirementAccess().getEqualsSignKeyword_1_1_1());
            	        
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1966:1: ( (lv_requiredState_7_0= RULE_STRING ) )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1967:1: (lv_requiredState_7_0= RULE_STRING )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1967:1: (lv_requiredState_7_0= RULE_STRING )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:1968:3: lv_requiredState_7_0= RULE_STRING
            	    {
            	    lv_requiredState_7_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleStateRequirement4813); 

            	    			newLeafNode(lv_requiredState_7_0, grammarAccess.getStateRequirementAccess().getRequiredStateSTRINGTerminalRuleCall_1_1_2_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getStateRequirementRule());
            	    	        }
            	           		setWithLastConsumed(
            	           			current, 
            	           			"requiredState",
            	            		lv_requiredState_7_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1());
            	    	 				

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt20 >= 1 ) break loop20;
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
            } while (true);

            if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1()) ) {
                throw new FailedPredicateException(input, "ruleStateRequirement", "getUnorderedGroupHelper().canLeave(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1())");
            }

            }


            }

             
            	  getUnorderedGroupHelper().leave(grammarAccess.getStateRequirementAccess().getUnorderedGroup_1());
            	

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStateRequirement"


    // $ANTLR start "entryRuleEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2007:1: entryRuleEffect returns [String current=null] : iv_ruleEffect= ruleEffect EOF ;
    public final String entryRuleEffect() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEffect = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2008:2: (iv_ruleEffect= ruleEffect EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2009:2: iv_ruleEffect= ruleEffect EOF
            {
             newCompositeNode(grammarAccess.getEffectRule()); 
            pushFollow(FOLLOW_ruleEffect_in_entryRuleEffect4902);
            iv_ruleEffect=ruleEffect();

            state._fsp--;

             current =iv_ruleEffect.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleEffect4913); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEffect"


    // $ANTLR start "ruleEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2016:1: ruleEffect returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_NumericEffect_0= ruleNumericEffect | this_StateEffect_1= ruleStateEffect | this_ClaimableEffect_2= ruleClaimableEffect | this_SharedEffect_3= ruleSharedEffect ) ;
    public final AntlrDatatypeRuleToken ruleEffect() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        AntlrDatatypeRuleToken this_NumericEffect_0 = null;

        AntlrDatatypeRuleToken this_StateEffect_1 = null;

        AntlrDatatypeRuleToken this_ClaimableEffect_2 = null;

        AntlrDatatypeRuleToken this_SharedEffect_3 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2019:28: ( (this_NumericEffect_0= ruleNumericEffect | this_StateEffect_1= ruleStateEffect | this_ClaimableEffect_2= ruleClaimableEffect | this_SharedEffect_3= ruleSharedEffect ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2020:1: (this_NumericEffect_0= ruleNumericEffect | this_StateEffect_1= ruleStateEffect | this_ClaimableEffect_2= ruleClaimableEffect | this_SharedEffect_3= ruleSharedEffect )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2020:1: (this_NumericEffect_0= ruleNumericEffect | this_StateEffect_1= ruleStateEffect | this_ClaimableEffect_2= ruleClaimableEffect | this_SharedEffect_3= ruleSharedEffect )
            int alt21=4;
            switch ( input.LA(1) ) {
            case 44:
                {
                alt21=1;
                }
                break;
            case 45:
                {
                alt21=2;
                }
                break;
            case 46:
                {
                alt21=3;
                }
                break;
            case 47:
                {
                alt21=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2021:5: this_NumericEffect_0= ruleNumericEffect
                    {
                     
                            newCompositeNode(grammarAccess.getEffectAccess().getNumericEffectParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleNumericEffect_in_ruleEffect4960);
                    this_NumericEffect_0=ruleNumericEffect();

                    state._fsp--;


                    		current.merge(this_NumericEffect_0);
                        
                     
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2033:5: this_StateEffect_1= ruleStateEffect
                    {
                     
                            newCompositeNode(grammarAccess.getEffectAccess().getStateEffectParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleStateEffect_in_ruleEffect4993);
                    this_StateEffect_1=ruleStateEffect();

                    state._fsp--;


                    		current.merge(this_StateEffect_1);
                        
                     
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 3 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2045:5: this_ClaimableEffect_2= ruleClaimableEffect
                    {
                     
                            newCompositeNode(grammarAccess.getEffectAccess().getClaimableEffectParserRuleCall_2()); 
                        
                    pushFollow(FOLLOW_ruleClaimableEffect_in_ruleEffect5026);
                    this_ClaimableEffect_2=ruleClaimableEffect();

                    state._fsp--;


                    		current.merge(this_ClaimableEffect_2);
                        
                     
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 4 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2057:5: this_SharedEffect_3= ruleSharedEffect
                    {
                     
                            newCompositeNode(grammarAccess.getEffectAccess().getSharedEffectParserRuleCall_3()); 
                        
                    pushFollow(FOLLOW_ruleSharedEffect_in_ruleEffect5059);
                    this_SharedEffect_3=ruleSharedEffect();

                    state._fsp--;


                    		current.merge(this_SharedEffect_3);
                        
                     
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEffect"


    // $ANTLR start "entryRuleNumericEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2075:1: entryRuleNumericEffect returns [String current=null] : iv_ruleNumericEffect= ruleNumericEffect EOF ;
    public final String entryRuleNumericEffect() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleNumericEffect = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2076:2: (iv_ruleNumericEffect= ruleNumericEffect EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2077:2: iv_ruleNumericEffect= ruleNumericEffect EOF
            {
             newCompositeNode(grammarAccess.getNumericEffectRule()); 
            pushFollow(FOLLOW_ruleNumericEffect_in_entryRuleNumericEffect5105);
            iv_ruleNumericEffect=ruleNumericEffect();

            state._fsp--;

             current =iv_ruleNumericEffect.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNumericEffect5116); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNumericEffect"


    // $ANTLR start "ruleNumericEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2084:1: ruleNumericEffect returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'numericEffect' ;
    public final AntlrDatatypeRuleToken ruleNumericEffect() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2087:28: (kw= 'numericEffect' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2089:2: kw= 'numericEffect'
            {
            kw=(Token)match(input,44,FOLLOW_44_in_ruleNumericEffect5153); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getNumericEffectAccess().getNumericEffectKeyword()); 
                

            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNumericEffect"


    // $ANTLR start "entryRuleStateEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2102:1: entryRuleStateEffect returns [String current=null] : iv_ruleStateEffect= ruleStateEffect EOF ;
    public final String entryRuleStateEffect() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleStateEffect = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2103:2: (iv_ruleStateEffect= ruleStateEffect EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2104:2: iv_ruleStateEffect= ruleStateEffect EOF
            {
             newCompositeNode(grammarAccess.getStateEffectRule()); 
            pushFollow(FOLLOW_ruleStateEffect_in_entryRuleStateEffect5193);
            iv_ruleStateEffect=ruleStateEffect();

            state._fsp--;

             current =iv_ruleStateEffect.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleStateEffect5204); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStateEffect"


    // $ANTLR start "ruleStateEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2111:1: ruleStateEffect returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'stateEffect' ;
    public final AntlrDatatypeRuleToken ruleStateEffect() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2114:28: (kw= 'stateEffect' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2116:2: kw= 'stateEffect'
            {
            kw=(Token)match(input,45,FOLLOW_45_in_ruleStateEffect5241); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getStateEffectAccess().getStateEffectKeyword()); 
                

            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStateEffect"


    // $ANTLR start "entryRuleClaimableEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2129:1: entryRuleClaimableEffect returns [String current=null] : iv_ruleClaimableEffect= ruleClaimableEffect EOF ;
    public final String entryRuleClaimableEffect() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleClaimableEffect = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2130:2: (iv_ruleClaimableEffect= ruleClaimableEffect EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2131:2: iv_ruleClaimableEffect= ruleClaimableEffect EOF
            {
             newCompositeNode(grammarAccess.getClaimableEffectRule()); 
            pushFollow(FOLLOW_ruleClaimableEffect_in_entryRuleClaimableEffect5281);
            iv_ruleClaimableEffect=ruleClaimableEffect();

            state._fsp--;

             current =iv_ruleClaimableEffect.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleClaimableEffect5292); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleClaimableEffect"


    // $ANTLR start "ruleClaimableEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2138:1: ruleClaimableEffect returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'claimableEffect' ;
    public final AntlrDatatypeRuleToken ruleClaimableEffect() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2141:28: (kw= 'claimableEffect' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2143:2: kw= 'claimableEffect'
            {
            kw=(Token)match(input,46,FOLLOW_46_in_ruleClaimableEffect5329); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getClaimableEffectAccess().getClaimableEffectKeyword()); 
                

            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleClaimableEffect"


    // $ANTLR start "entryRuleSharedEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2156:1: entryRuleSharedEffect returns [String current=null] : iv_ruleSharedEffect= ruleSharedEffect EOF ;
    public final String entryRuleSharedEffect() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSharedEffect = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2157:2: (iv_ruleSharedEffect= ruleSharedEffect EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2158:2: iv_ruleSharedEffect= ruleSharedEffect EOF
            {
             newCompositeNode(grammarAccess.getSharedEffectRule()); 
            pushFollow(FOLLOW_ruleSharedEffect_in_entryRuleSharedEffect5369);
            iv_ruleSharedEffect=ruleSharedEffect();

            state._fsp--;

             current =iv_ruleSharedEffect.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSharedEffect5380); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSharedEffect"


    // $ANTLR start "ruleSharedEffect"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2165:1: ruleSharedEffect returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'sharedEffect' ;
    public final AntlrDatatypeRuleToken ruleSharedEffect() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2168:28: (kw= 'sharedEffect' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2170:2: kw= 'sharedEffect'
            {
            kw=(Token)match(input,47,FOLLOW_47_in_ruleSharedEffect5417); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getSharedEffectAccess().getSharedEffectKeyword()); 
                

            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSharedEffect"


    // $ANTLR start "entryRuleActivityGroupDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2183:1: entryRuleActivityGroupDef returns [EObject current=null] : iv_ruleActivityGroupDef= ruleActivityGroupDef EOF ;
    public final EObject entryRuleActivityGroupDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleActivityGroupDef = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2184:2: (iv_ruleActivityGroupDef= ruleActivityGroupDef EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2185:2: iv_ruleActivityGroupDef= ruleActivityGroupDef EOF
            {
             newCompositeNode(grammarAccess.getActivityGroupDefRule()); 
            pushFollow(FOLLOW_ruleActivityGroupDef_in_entryRuleActivityGroupDef5456);
            iv_ruleActivityGroupDef=ruleActivityGroupDef();

            state._fsp--;

             current =iv_ruleActivityGroupDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleActivityGroupDef5466); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleActivityGroupDef"


    // $ANTLR start "ruleActivityGroupDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2192:1: ruleActivityGroupDef returns [EObject current=null] : (otherlv_0= 'ActivityGroupDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_annotations_3_0= ruleAnnotation ) )* ( (lv_parameters_4_0= ruleParameterDef ) )* otherlv_5= '}' ) ;
    public final EObject ruleActivityGroupDef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_5=null;
        EObject lv_annotations_3_0 = null;

        EObject lv_parameters_4_0 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2195:28: ( (otherlv_0= 'ActivityGroupDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_annotations_3_0= ruleAnnotation ) )* ( (lv_parameters_4_0= ruleParameterDef ) )* otherlv_5= '}' ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2196:1: (otherlv_0= 'ActivityGroupDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_annotations_3_0= ruleAnnotation ) )* ( (lv_parameters_4_0= ruleParameterDef ) )* otherlv_5= '}' )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2196:1: (otherlv_0= 'ActivityGroupDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_annotations_3_0= ruleAnnotation ) )* ( (lv_parameters_4_0= ruleParameterDef ) )* otherlv_5= '}' )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2196:3: otherlv_0= 'ActivityGroupDef' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_annotations_3_0= ruleAnnotation ) )* ( (lv_parameters_4_0= ruleParameterDef ) )* otherlv_5= '}'
            {
            otherlv_0=(Token)match(input,48,FOLLOW_48_in_ruleActivityGroupDef5503); 

                	newLeafNode(otherlv_0, grammarAccess.getActivityGroupDefAccess().getActivityGroupDefKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2200:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2201:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2201:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2202:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleActivityGroupDef5520); 

            			newLeafNode(lv_name_1_0, grammarAccess.getActivityGroupDefAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getActivityGroupDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleActivityGroupDef5537); 

                	newLeafNode(otherlv_2, grammarAccess.getActivityGroupDefAccess().getLeftCurlyBracketKeyword_2());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2222:1: ( (lv_annotations_3_0= ruleAnnotation ) )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==38) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2223:1: (lv_annotations_3_0= ruleAnnotation )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2223:1: (lv_annotations_3_0= ruleAnnotation )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2224:3: lv_annotations_3_0= ruleAnnotation
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getActivityGroupDefAccess().getAnnotationsAnnotationParserRuleCall_3_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleAnnotation_in_ruleActivityGroupDef5558);
            	    lv_annotations_3_0=ruleAnnotation();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getActivityGroupDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"annotations",
            	            		lv_annotations_3_0, 
            	            		"Annotation");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2240:3: ( (lv_parameters_4_0= ruleParameterDef ) )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==24||LA23_0==31) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2241:1: (lv_parameters_4_0= ruleParameterDef )
            	    {
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2241:1: (lv_parameters_4_0= ruleParameterDef )
            	    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2242:3: lv_parameters_4_0= ruleParameterDef
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getActivityGroupDefAccess().getParametersParameterDefParserRuleCall_4_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleParameterDef_in_ruleActivityGroupDef5580);
            	    lv_parameters_4_0=ruleParameterDef();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getActivityGroupDefRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"parameters",
            	            		lv_parameters_4_0, 
            	            		"ParameterDef");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);

            otherlv_5=(Token)match(input,20,FOLLOW_20_in_ruleActivityGroupDef5593); 

                	newLeafNode(otherlv_5, grammarAccess.getActivityGroupDefAccess().getRightCurlyBracketKeyword_5());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleActivityGroupDef"


    // $ANTLR start "entryRuleObjectDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2270:1: entryRuleObjectDef returns [EObject current=null] : iv_ruleObjectDef= ruleObjectDef EOF ;
    public final EObject entryRuleObjectDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObjectDef = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2271:2: (iv_ruleObjectDef= ruleObjectDef EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2272:2: iv_ruleObjectDef= ruleObjectDef EOF
            {
             newCompositeNode(grammarAccess.getObjectDefRule()); 
            pushFollow(FOLLOW_ruleObjectDef_in_entryRuleObjectDef5629);
            iv_ruleObjectDef=ruleObjectDef();

            state._fsp--;

             current =iv_ruleObjectDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleObjectDef5639); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleObjectDef"


    // $ANTLR start "ruleObjectDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2279:1: ruleObjectDef returns [EObject current=null] : (otherlv_0= 'ObjectDef' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleObjectDef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2282:28: ( (otherlv_0= 'ObjectDef' ( (lv_name_1_0= RULE_ID ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2283:1: (otherlv_0= 'ObjectDef' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2283:1: (otherlv_0= 'ObjectDef' ( (lv_name_1_0= RULE_ID ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2283:3: otherlv_0= 'ObjectDef' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,49,FOLLOW_49_in_ruleObjectDef5676); 

                	newLeafNode(otherlv_0, grammarAccess.getObjectDefAccess().getObjectDefKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2287:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2288:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2288:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2289:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectDef5693); 

            			newLeafNode(lv_name_1_0, grammarAccess.getObjectDefAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getObjectDefRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleObjectDef"


    // $ANTLR start "entryRuleResourceDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2313:1: entryRuleResourceDef returns [EObject current=null] : iv_ruleResourceDef= ruleResourceDef EOF ;
    public final EObject entryRuleResourceDef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleResourceDef = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2314:2: (iv_ruleResourceDef= ruleResourceDef EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2315:2: iv_ruleResourceDef= ruleResourceDef EOF
            {
             newCompositeNode(grammarAccess.getResourceDefRule()); 
            pushFollow(FOLLOW_ruleResourceDef_in_entryRuleResourceDef5734);
            iv_ruleResourceDef=ruleResourceDef();

            state._fsp--;

             current =iv_ruleResourceDef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleResourceDef5744); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleResourceDef"


    // $ANTLR start "ruleResourceDef"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2322:1: ruleResourceDef returns [EObject current=null] : (this_NumericResource_0= ruleNumericResource | this_StateResource_1= ruleStateResource | this_ClaimableResource_2= ruleClaimableResource | this_SharableResource_3= ruleSharableResource | this_SummaryResource_4= ruleSummaryResource ) ;
    public final EObject ruleResourceDef() throws RecognitionException {
        EObject current = null;

        EObject this_NumericResource_0 = null;

        EObject this_StateResource_1 = null;

        EObject this_ClaimableResource_2 = null;

        EObject this_SharableResource_3 = null;

        EObject this_SummaryResource_4 = null;


         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2325:28: ( (this_NumericResource_0= ruleNumericResource | this_StateResource_1= ruleStateResource | this_ClaimableResource_2= ruleClaimableResource | this_SharableResource_3= ruleSharableResource | this_SummaryResource_4= ruleSummaryResource ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2326:1: (this_NumericResource_0= ruleNumericResource | this_StateResource_1= ruleStateResource | this_ClaimableResource_2= ruleClaimableResource | this_SharableResource_3= ruleSharableResource | this_SummaryResource_4= ruleSummaryResource )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2326:1: (this_NumericResource_0= ruleNumericResource | this_StateResource_1= ruleStateResource | this_ClaimableResource_2= ruleClaimableResource | this_SharableResource_3= ruleSharableResource | this_SummaryResource_4= ruleSummaryResource )
            int alt24=5;
            switch ( input.LA(1) ) {
            case 50:
                {
                alt24=1;
                }
                break;
            case 51:
                {
                alt24=2;
                }
                break;
            case 52:
                {
                alt24=3;
                }
                break;
            case 53:
                {
                alt24=4;
                }
                break;
            case 54:
                {
                alt24=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2327:5: this_NumericResource_0= ruleNumericResource
                    {
                     
                            newCompositeNode(grammarAccess.getResourceDefAccess().getNumericResourceParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleNumericResource_in_ruleResourceDef5791);
                    this_NumericResource_0=ruleNumericResource();

                    state._fsp--;

                     
                            current = this_NumericResource_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2337:5: this_StateResource_1= ruleStateResource
                    {
                     
                            newCompositeNode(grammarAccess.getResourceDefAccess().getStateResourceParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleStateResource_in_ruleResourceDef5818);
                    this_StateResource_1=ruleStateResource();

                    state._fsp--;

                     
                            current = this_StateResource_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 3 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2347:5: this_ClaimableResource_2= ruleClaimableResource
                    {
                     
                            newCompositeNode(grammarAccess.getResourceDefAccess().getClaimableResourceParserRuleCall_2()); 
                        
                    pushFollow(FOLLOW_ruleClaimableResource_in_ruleResourceDef5845);
                    this_ClaimableResource_2=ruleClaimableResource();

                    state._fsp--;

                     
                            current = this_ClaimableResource_2; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 4 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2357:5: this_SharableResource_3= ruleSharableResource
                    {
                     
                            newCompositeNode(grammarAccess.getResourceDefAccess().getSharableResourceParserRuleCall_3()); 
                        
                    pushFollow(FOLLOW_ruleSharableResource_in_ruleResourceDef5872);
                    this_SharableResource_3=ruleSharableResource();

                    state._fsp--;

                     
                            current = this_SharableResource_3; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 5 :
                    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2367:5: this_SummaryResource_4= ruleSummaryResource
                    {
                     
                            newCompositeNode(grammarAccess.getResourceDefAccess().getSummaryResourceParserRuleCall_4()); 
                        
                    pushFollow(FOLLOW_ruleSummaryResource_in_ruleResourceDef5899);
                    this_SummaryResource_4=ruleSummaryResource();

                    state._fsp--;

                     
                            current = this_SummaryResource_4; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleResourceDef"


    // $ANTLR start "entryRuleNumericResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2383:1: entryRuleNumericResource returns [EObject current=null] : iv_ruleNumericResource= ruleNumericResource EOF ;
    public final EObject entryRuleNumericResource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNumericResource = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2384:2: (iv_ruleNumericResource= ruleNumericResource EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2385:2: iv_ruleNumericResource= ruleNumericResource EOF
            {
             newCompositeNode(grammarAccess.getNumericResourceRule()); 
            pushFollow(FOLLOW_ruleNumericResource_in_entryRuleNumericResource5934);
            iv_ruleNumericResource=ruleNumericResource();

            state._fsp--;

             current =iv_ruleNumericResource; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNumericResource5944); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNumericResource"


    // $ANTLR start "ruleNumericResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2392:1: ruleNumericResource returns [EObject current=null] : (otherlv_0= 'NumericResource' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleNumericResource() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2395:28: ( (otherlv_0= 'NumericResource' ( (lv_name_1_0= RULE_ID ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2396:1: (otherlv_0= 'NumericResource' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2396:1: (otherlv_0= 'NumericResource' ( (lv_name_1_0= RULE_ID ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2396:3: otherlv_0= 'NumericResource' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,50,FOLLOW_50_in_ruleNumericResource5981); 

                	newLeafNode(otherlv_0, grammarAccess.getNumericResourceAccess().getNumericResourceKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2400:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2401:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2401:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2402:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleNumericResource5998); 

            			newLeafNode(lv_name_1_0, grammarAccess.getNumericResourceAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getNumericResourceRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNumericResource"


    // $ANTLR start "entryRuleStateResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2426:1: entryRuleStateResource returns [EObject current=null] : iv_ruleStateResource= ruleStateResource EOF ;
    public final EObject entryRuleStateResource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStateResource = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2427:2: (iv_ruleStateResource= ruleStateResource EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2428:2: iv_ruleStateResource= ruleStateResource EOF
            {
             newCompositeNode(grammarAccess.getStateResourceRule()); 
            pushFollow(FOLLOW_ruleStateResource_in_entryRuleStateResource6039);
            iv_ruleStateResource=ruleStateResource();

            state._fsp--;

             current =iv_ruleStateResource; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleStateResource6049); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStateResource"


    // $ANTLR start "ruleStateResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2435:1: ruleStateResource returns [EObject current=null] : (otherlv_0= 'StateResource' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleStateResource() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2438:28: ( (otherlv_0= 'StateResource' ( (lv_name_1_0= RULE_ID ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2439:1: (otherlv_0= 'StateResource' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2439:1: (otherlv_0= 'StateResource' ( (lv_name_1_0= RULE_ID ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2439:3: otherlv_0= 'StateResource' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,51,FOLLOW_51_in_ruleStateResource6086); 

                	newLeafNode(otherlv_0, grammarAccess.getStateResourceAccess().getStateResourceKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2443:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2444:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2444:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2445:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleStateResource6103); 

            			newLeafNode(lv_name_1_0, grammarAccess.getStateResourceAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getStateResourceRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStateResource"


    // $ANTLR start "entryRuleClaimableResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2469:1: entryRuleClaimableResource returns [EObject current=null] : iv_ruleClaimableResource= ruleClaimableResource EOF ;
    public final EObject entryRuleClaimableResource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleClaimableResource = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2470:2: (iv_ruleClaimableResource= ruleClaimableResource EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2471:2: iv_ruleClaimableResource= ruleClaimableResource EOF
            {
             newCompositeNode(grammarAccess.getClaimableResourceRule()); 
            pushFollow(FOLLOW_ruleClaimableResource_in_entryRuleClaimableResource6144);
            iv_ruleClaimableResource=ruleClaimableResource();

            state._fsp--;

             current =iv_ruleClaimableResource; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleClaimableResource6154); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleClaimableResource"


    // $ANTLR start "ruleClaimableResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2478:1: ruleClaimableResource returns [EObject current=null] : (otherlv_0= 'ClaimableResource' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleClaimableResource() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2481:28: ( (otherlv_0= 'ClaimableResource' ( (lv_name_1_0= RULE_ID ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2482:1: (otherlv_0= 'ClaimableResource' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2482:1: (otherlv_0= 'ClaimableResource' ( (lv_name_1_0= RULE_ID ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2482:3: otherlv_0= 'ClaimableResource' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,52,FOLLOW_52_in_ruleClaimableResource6191); 

                	newLeafNode(otherlv_0, grammarAccess.getClaimableResourceAccess().getClaimableResourceKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2486:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2487:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2487:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2488:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleClaimableResource6208); 

            			newLeafNode(lv_name_1_0, grammarAccess.getClaimableResourceAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getClaimableResourceRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleClaimableResource"


    // $ANTLR start "entryRuleSharableResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2512:1: entryRuleSharableResource returns [EObject current=null] : iv_ruleSharableResource= ruleSharableResource EOF ;
    public final EObject entryRuleSharableResource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSharableResource = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2513:2: (iv_ruleSharableResource= ruleSharableResource EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2514:2: iv_ruleSharableResource= ruleSharableResource EOF
            {
             newCompositeNode(grammarAccess.getSharableResourceRule()); 
            pushFollow(FOLLOW_ruleSharableResource_in_entryRuleSharableResource6249);
            iv_ruleSharableResource=ruleSharableResource();

            state._fsp--;

             current =iv_ruleSharableResource; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSharableResource6259); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSharableResource"


    // $ANTLR start "ruleSharableResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2521:1: ruleSharableResource returns [EObject current=null] : (otherlv_0= 'SharableResource' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleSharableResource() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2524:28: ( (otherlv_0= 'SharableResource' ( (lv_name_1_0= RULE_ID ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2525:1: (otherlv_0= 'SharableResource' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2525:1: (otherlv_0= 'SharableResource' ( (lv_name_1_0= RULE_ID ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2525:3: otherlv_0= 'SharableResource' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,53,FOLLOW_53_in_ruleSharableResource6296); 

                	newLeafNode(otherlv_0, grammarAccess.getSharableResourceAccess().getSharableResourceKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2529:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2530:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2530:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2531:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSharableResource6313); 

            			newLeafNode(lv_name_1_0, grammarAccess.getSharableResourceAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSharableResourceRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSharableResource"


    // $ANTLR start "entryRuleSummaryResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2555:1: entryRuleSummaryResource returns [EObject current=null] : iv_ruleSummaryResource= ruleSummaryResource EOF ;
    public final EObject entryRuleSummaryResource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSummaryResource = null;


        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2556:2: (iv_ruleSummaryResource= ruleSummaryResource EOF )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2557:2: iv_ruleSummaryResource= ruleSummaryResource EOF
            {
             newCompositeNode(grammarAccess.getSummaryResourceRule()); 
            pushFollow(FOLLOW_ruleSummaryResource_in_entryRuleSummaryResource6354);
            iv_ruleSummaryResource=ruleSummaryResource();

            state._fsp--;

             current =iv_ruleSummaryResource; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSummaryResource6364); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSummaryResource"


    // $ANTLR start "ruleSummaryResource"
    // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2564:1: ruleSummaryResource returns [EObject current=null] : (otherlv_0= 'Summaryresource' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleSummaryResource() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;

         enterRule(); 
            
        try {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2567:28: ( (otherlv_0= 'Summaryresource' ( (lv_name_1_0= RULE_ID ) ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2568:1: (otherlv_0= 'Summaryresource' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2568:1: (otherlv_0= 'Summaryresource' ( (lv_name_1_0= RULE_ID ) ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2568:3: otherlv_0= 'Summaryresource' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,54,FOLLOW_54_in_ruleSummaryResource6401); 

                	newLeafNode(otherlv_0, grammarAccess.getSummaryResourceAccess().getSummaryresourceKeyword_0());
                
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2572:1: ( (lv_name_1_0= RULE_ID ) )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2573:1: (lv_name_1_0= RULE_ID )
            {
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2573:1: (lv_name_1_0= RULE_ID )
            // ../gov.nasa.ensemble.dictionary.xtext/src-gen/gov/nasa/ensemble/dictionary/xtext/parser/antlr/internal/InternalXDictionary.g:2574:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSummaryResource6418); 

            			newLeafNode(lv_name_1_0, grammarAccess.getSummaryResourceAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSummaryResourceRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSummaryResource"

    // Delegated rules


 

    public static final BitSet FOLLOW_ruleDictionary_in_entryRuleDictionary75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDictionary85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_ruleDictionary168 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleDictionary180 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleDictionary197 = new BitSet(new long[]{0x007F00088107E802L});
    public static final BitSet FOLLOW_13_in_ruleDictionary270 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleDictionary282 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleDictionary299 = new BitSet(new long[]{0x007F00088107E802L});
    public static final BitSet FOLLOW_14_in_ruleDictionary372 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleDictionary384 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleDictionary401 = new BitSet(new long[]{0x007F00088107E802L});
    public static final BitSet FOLLOW_15_in_ruleDictionary474 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleDictionary486 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleDictionary503 = new BitSet(new long[]{0x007F00088107E802L});
    public static final BitSet FOLLOW_16_in_ruleDictionary576 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleDictionary588 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleDictionary605 = new BitSet(new long[]{0x007F00088107E802L});
    public static final BitSet FOLLOW_17_in_ruleDictionary678 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleDictionary690 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleDictionary707 = new BitSet(new long[]{0x007F00088107E802L});
    public static final BitSet FOLLOW_ruleDefinition_in_ruleDictionary780 = new BitSet(new long[]{0x007F000881040002L});
    public static final BitSet FOLLOW_ruleDefinition_in_entryRuleDefinition817 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDefinition827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEnumDef_in_ruleDefinition874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterDef_in_ruleDefinition901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleActivityDef_in_ruleDefinition928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleActivityGroupDef_in_ruleDefinition955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleObjectDef_in_ruleDefinition982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleResourceDef_in_ruleDefinition1009 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEnumDef_in_entryRuleEnumDef1044 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleEnumDef1054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleEnumDef1091 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleEnumDef1108 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleEnumDef1125 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_ruleEnumValue_in_ruleEnumDef1146 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_20_in_ruleEnumDef1159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEnumValue_in_entryRuleEnumValue1195 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleEnumValue1205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_ruleEnumValue1242 = new BitSet(new long[]{0x0000000000C00800L});
    public static final BitSet FOLLOW_11_in_ruleEnumValue1300 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleEnumValue1312 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleEnumValue1329 = new BitSet(new long[]{0x0000000000C00802L});
    public static final BitSet FOLLOW_22_in_ruleEnumValue1402 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleEnumValue1414 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleEnumValue1431 = new BitSet(new long[]{0x0000000000C00802L});
    public static final BitSet FOLLOW_23_in_ruleEnumValue1504 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleEnumValue1516 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleEnumValue1533 = new BitSet(new long[]{0x0000000000C00802L});
    public static final BitSet FOLLOW_ruleParameterDef_in_entryRuleParameterDef1621 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameterDef1631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAttributeDef_in_ruleParameterDef1678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleReferenceDef_in_ruleParameterDef1705 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAttributeDef_in_entryRuleAttributeDef1740 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAttributeDef1750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_ruleAttributeDef1787 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAttributeDef1804 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAttributeDef1826 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleAttributeDef1843 = new BitSet(new long[]{0x000000407E108000L});
    public static final BitSet FOLLOW_25_in_ruleAttributeDef1901 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleAttributeDef1913 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAttributeDef1930 = new BitSet(new long[]{0x000000407E108000L});
    public static final BitSet FOLLOW_15_in_ruleAttributeDef2003 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleAttributeDef2015 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAttributeDef2032 = new BitSet(new long[]{0x000000407E108000L});
    public static final BitSet FOLLOW_26_in_ruleAttributeDef2105 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleAttributeDef2117 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAttributeDef2134 = new BitSet(new long[]{0x000000407E108000L});
    public static final BitSet FOLLOW_27_in_ruleAttributeDef2207 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleAttributeDef2219 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAttributeDef2236 = new BitSet(new long[]{0x000000407E108000L});
    public static final BitSet FOLLOW_28_in_ruleAttributeDef2309 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleAttributeDef2321 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAttributeDef2338 = new BitSet(new long[]{0x000000407E108000L});
    public static final BitSet FOLLOW_29_in_ruleAttributeDef2411 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleAttributeDef2423 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAttributeDef2440 = new BitSet(new long[]{0x000000407E108000L});
    public static final BitSet FOLLOW_30_in_ruleAttributeDef2513 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleAttributeDef2525 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAttributeDef2542 = new BitSet(new long[]{0x000000407E108000L});
    public static final BitSet FOLLOW_ruleAnnotation_in_ruleAttributeDef2609 = new BitSet(new long[]{0x0000004000100000L});
    public static final BitSet FOLLOW_20_in_ruleAttributeDef2622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleReferenceDef_in_entryRuleReferenceDef2658 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleReferenceDef2668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_ruleReferenceDef2705 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleReferenceDef2722 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleReferenceDef2744 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleReferenceDef2761 = new BitSet(new long[]{0x0000F2C130108000L});
    public static final BitSet FOLLOW_15_in_ruleReferenceDef2819 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleReferenceDef2831 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleReferenceDef2848 = new BitSet(new long[]{0x0000F2C130108000L});
    public static final BitSet FOLLOW_28_in_ruleReferenceDef2921 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleReferenceDef2933 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleReferenceDef2950 = new BitSet(new long[]{0x0000F2C130108000L});
    public static final BitSet FOLLOW_29_in_ruleReferenceDef3023 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleReferenceDef3035 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleReferenceDef3052 = new BitSet(new long[]{0x0000F2C130108000L});
    public static final BitSet FOLLOW_32_in_ruleReferenceDef3125 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleReferenceDef3137 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_ruleBoolean_in_ruleReferenceDef3158 = new BitSet(new long[]{0x0000F2C130108000L});
    public static final BitSet FOLLOW_ruleAnnotation_in_ruleReferenceDef3220 = new BitSet(new long[]{0x0000F2C000100000L});
    public static final BitSet FOLLOW_ruleRequirement_in_ruleReferenceDef3242 = new BitSet(new long[]{0x0000F28000100000L});
    public static final BitSet FOLLOW_ruleEffect_in_ruleReferenceDef3264 = new BitSet(new long[]{0x0000F00000100000L});
    public static final BitSet FOLLOW_20_in_ruleReferenceDef3277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBoolean_in_entryRuleBoolean3314 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBoolean3325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_ruleBoolean3363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_ruleBoolean3382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleActivityDef_in_entryRuleActivityDef3422 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleActivityDef3432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_ruleActivityDef3469 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleActivityDef3486 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleActivityDef3503 = new BitSet(new long[]{0x0000F2F0B1108000L});
    public static final BitSet FOLLOW_15_in_ruleActivityDef3561 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleActivityDef3573 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleActivityDef3590 = new BitSet(new long[]{0x0000F2F0B1108000L});
    public static final BitSet FOLLOW_29_in_ruleActivityDef3663 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleActivityDef3675 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleActivityDef3692 = new BitSet(new long[]{0x0000F2F0B1108000L});
    public static final BitSet FOLLOW_36_in_ruleActivityDef3765 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleActivityDef3777 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleActivityDef3794 = new BitSet(new long[]{0x0000F2F0B1108000L});
    public static final BitSet FOLLOW_28_in_ruleActivityDef3867 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleActivityDef3879 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleActivityDef3896 = new BitSet(new long[]{0x0000F2F0B1108000L});
    public static final BitSet FOLLOW_37_in_ruleActivityDef3969 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleActivityDef3981 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleActivityDef3998 = new BitSet(new long[]{0x0000F2F0B1108000L});
    public static final BitSet FOLLOW_ruleAnnotation_in_ruleActivityDef4065 = new BitSet(new long[]{0x0000F2C081100000L});
    public static final BitSet FOLLOW_ruleParameterDef_in_ruleActivityDef4087 = new BitSet(new long[]{0x0000F28081100000L});
    public static final BitSet FOLLOW_ruleRequirement_in_ruleActivityDef4109 = new BitSet(new long[]{0x0000F28000100000L});
    public static final BitSet FOLLOW_ruleEffect_in_ruleActivityDef4131 = new BitSet(new long[]{0x0000F00000100000L});
    public static final BitSet FOLLOW_20_in_ruleActivityDef4144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAnnotation_in_entryRuleAnnotation4180 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAnnotation4190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_ruleAnnotation4227 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAnnotation4244 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAnnotation4266 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAnnotation4288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleRequirement_in_entryRuleRequirement4329 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleRequirement4339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNumericRequirement_in_ruleRequirement4386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStateRequirement_in_ruleRequirement4413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNumericRequirement_in_entryRuleNumericRequirement4448 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNumericRequirement4458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_ruleNumericRequirement4495 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_ruleNumericRequirement4507 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleNumericRequirement4519 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleNumericRequirement4536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStateRequirement_in_entryRuleStateRequirement4577 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleStateRequirement4587 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_ruleStateRequirement4624 = new BitSet(new long[]{0x00000C0000000000L});
    public static final BitSet FOLLOW_42_in_ruleStateRequirement4682 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleStateRequirement4694 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleStateRequirement4711 = new BitSet(new long[]{0x00000C0000000002L});
    public static final BitSet FOLLOW_43_in_ruleStateRequirement4784 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleStateRequirement4796 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleStateRequirement4813 = new BitSet(new long[]{0x00000C0000000002L});
    public static final BitSet FOLLOW_ruleEffect_in_entryRuleEffect4902 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleEffect4913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNumericEffect_in_ruleEffect4960 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStateEffect_in_ruleEffect4993 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClaimableEffect_in_ruleEffect5026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSharedEffect_in_ruleEffect5059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNumericEffect_in_entryRuleNumericEffect5105 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNumericEffect5116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_ruleNumericEffect5153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStateEffect_in_entryRuleStateEffect5193 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleStateEffect5204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_ruleStateEffect5241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClaimableEffect_in_entryRuleClaimableEffect5281 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleClaimableEffect5292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_ruleClaimableEffect5329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSharedEffect_in_entryRuleSharedEffect5369 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSharedEffect5380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_ruleSharedEffect5417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleActivityGroupDef_in_entryRuleActivityGroupDef5456 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleActivityGroupDef5466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_ruleActivityGroupDef5503 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleActivityGroupDef5520 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleActivityGroupDef5537 = new BitSet(new long[]{0x0000004081100000L});
    public static final BitSet FOLLOW_ruleAnnotation_in_ruleActivityGroupDef5558 = new BitSet(new long[]{0x0000004081100000L});
    public static final BitSet FOLLOW_ruleParameterDef_in_ruleActivityGroupDef5580 = new BitSet(new long[]{0x0000000081100000L});
    public static final BitSet FOLLOW_20_in_ruleActivityGroupDef5593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleObjectDef_in_entryRuleObjectDef5629 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleObjectDef5639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_ruleObjectDef5676 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectDef5693 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleResourceDef_in_entryRuleResourceDef5734 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleResourceDef5744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNumericResource_in_ruleResourceDef5791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStateResource_in_ruleResourceDef5818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClaimableResource_in_ruleResourceDef5845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSharableResource_in_ruleResourceDef5872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSummaryResource_in_ruleResourceDef5899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNumericResource_in_entryRuleNumericResource5934 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNumericResource5944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_ruleNumericResource5981 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleNumericResource5998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStateResource_in_entryRuleStateResource6039 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleStateResource6049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_ruleStateResource6086 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleStateResource6103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClaimableResource_in_entryRuleClaimableResource6144 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleClaimableResource6154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_ruleClaimableResource6191 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleClaimableResource6208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSharableResource_in_entryRuleSharableResource6249 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSharableResource6259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_ruleSharableResource6296 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSharableResource6313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSummaryResource_in_entryRuleSummaryResource6354 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSummaryResource6364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_ruleSummaryResource6401 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSummaryResource6418 = new BitSet(new long[]{0x0000000000000002L});

}
