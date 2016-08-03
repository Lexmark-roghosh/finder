package com.finder.helper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.finder.executor.DatabaseHandler;
import com.finder.executor.Execute;

public class ValidationUtils{
	
	private static final Logger logger =Logger.getLogger(ValidationUtils.class);
	
	public static final String[] webPageExtentions = new String[]{".dwt",".alx",".crt",".cer",".rss",".wsdl",".a4p",
			".dap",".dcr",".css",".ucf",".ascx",".crl",".dhtml",".kit",".shtm",".vrml",".xht",".sdb",
			".htaccess",".xhtm",".appcache",".zul",".browser",".dll",".site",".vrt",".htm",".csr",".cshtml",
			".xul",".website",".download",".php",".json",".asax",".asmx",".atom",".axd",".fcgi",".hype",
			".opml",".asa",".asr",".awm",".bml",".codasite",".epibrw",".fwp",".gne",".gsp",".idc",".oam",
			".ognc",".p7c",".pro",".rhtml",".rjs",".shtml",".sites",".sites2",".srf",".stc",".vbd",
			".vsdisco",".prf",".svc",".cms",".asp",".pac",".cfm",".maff",".a5w",".chm",".ap",
			".url",".aspx",".xhtml",".p12",".hxs",".muse",".swz",".wdgt",".xws",".compressed",".dothtml",
			".do",".webarchive",".mspx",".jnlp",".jsp",".html",".ashx",".spc",".obml",".vdw",".dbm",".dml",
			".fmp",".hdml",".master",".phtml",".sht",".wbs",".webarchivexml",".wgp",".widget",".xbl",".xss",
			".php4",".wpp",".xfdl",".webloc",".jspx",".nzb",".web",".seam",".con",".att",".qbo",".chat",
			".pem",".tvpi",".svr",".mht",".jsf",".wml",".adr",".wrf",".aro",".btapp",".cha",".der",".disco",
			".discomap",".edge",".esproj",".ewp",".hyperesources",".hypesymbol",".hypetemplate",".iwdgt",
			".jws",".lasso",".lbc",".mvc",".nod",".olp",".oth",".p7b",".qf",".rflw",".rwsw",".saveddeck",
			".scss",".suck",".vbhtml",".webbookmark",".whtt",".woa",".xbel",".xpd",".zhtml",".zhtml",".zvz",
			".csp",".phtm",".ppthtml",".pptmhtml",".stml",".htc",".uhtml",".aex",".htx",".mhtml",".tvvi",
			".dochtml",".webhistory",".vlp",".wgt",".bok",".stl",".rt",".docmhtml",".jhtml",".an",".less",
			".pub",".rwp",".sitemap",".ccbjs",".cfml",".ece",".iqy",".itms",".itpc",".jcz",".jspa",".jss",
			".jst",".mapx",".p7",".page",".ptw",".qrm",".rw3",".rwtheme",".ssp",".stm",".stp",".wbxml",".wn",
			".wpx",".psp",".zfo",".cpg",".nxg",".php3",".php5",".bwp",".cdf",".hdm",".jvs",".map",".moz",".mvr"};
	
	public static String[] imageFileExtentions = new String[]{".jpg",".JPG",".jpe",".JPE",".jpeg",".JPEG",".png",
			".PNG",".tif",".TIF",".RIF",".rif",".3dm",".tiff",".TIFF",".gif",".GIF",".bmp",".BMP",".ico",".psd"};
	
	public static String[] docFileExtentions = new String[]{".pdf",".PDF",".err",".sub",".save",".txt",".sty",".log",".mbox",
			".fdx",".lst",".wpd",".docx",".wtt",".bad",".bdr",".btd",".docm",".dvi",".emlx",".fadein",".fdr",
			".flr",".frt",".kes",".klg",".knt",".kon",".lis",".lxfml",".mellel",".mnt",".mwd",".mwp",".nfo",
			".safetext",".apt",".dsv",".sam",".wri",".man",".gdoc",".fbl",".aim",".text",".dropbox",".rtf",
			".lst",".dwd",".ans",".gpd",".hs",".rtfd",".me",".upd",".diz",".doc",".hht",".prt",".saf",".doc",
			".sig",".dotm",".pwi",".qdl",".tab",".xdl",".epp",".jp1",".wps",".ris",".vnt",".asc",".cod",
			".pages",".bdp",".boc",".dx",".fpt",".ltx",".pwd",".rpt",".template",".wpd",".eit",".fadein.template",
			".pfs",".pvm",".rtx",".tpc",".msg",".abw",".psw",".1st",".run",".dotx",".lp2",".eml",".odt",".tmd",
			".wpt",".xwp",".odm",".se",".latex",".wps",".tab",".prt",".wpl",".rst",".bib",".adoc",".ase",".aww",
			".bean",".bib",".bna",".bzabw",".calca",".charset",".chord",".cyi",".dne",".eio",".etf",".fcf",
			".fdt",".fdxt",".fodt",".fountain",".fwd",".gsd",".gthr",".gv",".ipspot",".klg",".kwd",".lbt",".luf",
			".lyx",".md5.txt",".mell",".nb",".njx",".nwp",".ofl",".ott",".pages-tef",".pjt",".plantuml",".pu",
			".qpf",".readme",".rzk",".rzn",".scriv",".scrivx",".sct",".scw",".sgm",".sla.gz",".story",".strings",
			".sublime-project",".sublime-workspace",".tdf",".tdf",".textclipping",".u3i",".unauth",".unx",".utf8",
			".utxt",".wpa",".xbdoc",".xdl",".xy3",".xyp",".xyw",".zabw",".zrtf",".tlb",".ascii",".docxml",".docz",
			".jarvis",".lyt",".now",".openbsd",".wtx",".xy",".tex",".sla",".sxw",".asc",".cnm",".emulecollection",
			".hwp",".p7s",".xwp",".hbk",".odo",".idx",".apkg",".fdf",".crwl",".notes",".rtd",".sdw",".sms",".ssa",
			".sam",".scc",".dca",".wp6",".lwp",".lnt",".uot",".vct",".webdoc",".act",".aty",".awp",".bbs",".bml",
			".brx",".cws",".dgs",".dxb",".dxp",".err",".fds",".gpn",".jis",".min",".mw",".ndoc",".ngloss",".nwctxt",
			".nwm",".ocr",".ort",".pdpcmd",".pfx",".pmo",".pvj",".pwdp",".pwdpl",".pwr",".scm",".sdm",".session",
			".skcard",".smf",".stw",".sxg",".tm",".tmv",".trelby",".tvj",".uof",".wbk",".wp",".wp4",".wp5",".wp7",
			".wpt",".xbplate",".hwp",".wpd",".zw",".awt",".chart",".emf",".gmd",".hz",".xwp",".dox",".dsc",".etx",
			".euc",".faq",".fft",".fwdn",".iil",".ipf",".joe",".jrtf",".ltr",".lue",".mcw",".odif",".rad",".rft",
			".sdoc",".thp",".vw",".wn",".wpw",".wsd",".nb",".xlsm",".xlsb",".xltm",".xltx",".ots",".xls",".edx",
			".gnumeric",".dex",".rdf",".gsheet",".xlsx",".xlr",".sdc",".fp",".ods",".pmd",".numbers",".xl",".123",
			".qpw",".tmv",".aws",".edxz",".fods",".gnm",".ncss",".numbers-tef",".uos",".xar",".wks",".ess",".imp",
			".xlshtml",".sxc",".ast",".bks",".def",".dfg",".fm",".mar",".nmbtemplate",".pmv",".stc",".tmvt",".fcs",
			".xlsmhtml",".wks",".dis",".wki",".wku",".wq1",".wq2",".xlthtml"};
	
	public static String[] audioFileExtentions = new String[]{".sng",".omf",".vag",".act",".vdj",".nra",".abm",
			".bnk",".hsb",".mscz",".rfl",".sma",".vyf",".ang",".acm",".at3",".aob",".syx",".aimppl",".nvf",".saf",
			".xfs",".mod",".sfk",".als",".gp5",".caf",".mp3",".pla",".wav",".sf2",".mid",".wma",".afc",".amxd",
			".dmsa",".dmse",".emp",".m4r",".midi",".ptx",".rns",".sib",".slp",".trak",".5xb",".a2b",".a2i",".agr",
			".akp",".asd",".aup",".bun",".bww",".copy",".csh",".dfc",".drg",".dsm",".dtm",".fev",".frg",".g726",
			".isma",".krz",".mbr",".minigsf",".mpga",".mtp",".musx",".nkc",".nkm",".omg",".pkf",".rex",".rip",
			".rol",".sbi",".sfpack",".smf",".smp",".sseq",".svd",".syh",".syw",".tg",".u",".uax",".vmd",".vpl",
			".zvd",".669",".eop",".m3u",".zab",".oga",".sty",".mus",".dig",".ogg",".aif",".flac",".aiff",".ins",
			".ksd",".mui",".dss",".rx2",".flp",".gpk",".aac",".logic",".5xe",".sd",".sdat",".flp",".pcg",".wave",
			".xspf",".cda",".amr",".3ga",".dcf",".nki",".aud",".cwt",".dls",".ds2",".flm",".gsm",".nsa",".ds",
			".pcm",".pho",".q1",".sph",".xwb",".dsp",".ins",".sam",".u8",".ym",".dff",".wve",".m3u8",".iff",
			".ram",".oma",".sds",".nsf",".acd",".ac3",".dsf",".cpr",".xa",".it",".vlc",".m4a",".acd-zip",".4mp",
			".apl",".cwp",".cws",".gsflib",".med",".mx5",".ply",".qcp",".r1m",".rmj",".stm",".w64",".wpk",".ahx",
			".au",".b4s",".hbb",".hbs",".ins",".kit",".kmp",".ksc",".mdl",".mu3",".q2",".sbg",".sfap0",".toc",
			".vgz",".vmf",".zpa",".2sf",".gpx",".ape",".fls",".mus",".emx",".ftm",".nbs",".pcast",".sesx",".dtshd",
			".mmm",".peak",".vox",".bmml",".mscx",".xmf",".rtm",".pls",".mo3",".mus",".xm",".avastsounds",".snd",
			".wax",".wpp",".ra",".seq",".aa",".m4b",".odm",".sfl",".mpa",".amz",".5xs",".a2m",".abc",".acd-bak",
			".adts",".agm",".aifc",".alc",".amf",".au",".band",".bap",".bdd",".bidule",".bwf",".caff",".cdda",
			".cdlx",".cdo",".cdr",".cel",".cgrp",".cidb",".ckb",".conform",".cpt",".cts",".cwb",".dct",".dewf",
			".df2",".dig",".dm",".dmf",".dra",".dwd",".efk",".efq",".efs",".efv",".emd",".esps",".f2r",".f32",
			".f3r",".f4a",".f64",".fdp",".fsb",".fsc",".fsm",".ftm",".ftmx",".fzf",".fzv",".g721",".gig",".gpbank",
			".groove",".gsf",".h4b",".h5b",".h5s",".hbe",".igp",".iti",".koz",".koz",".ksf",".kt3",".la",".lso",
			".lwv",".m4p",".ma1",".mdc",".mgv",".miniusf",".mka",".mmlp",".mmp",".mmpz",".mpc",".mte",".mti",
			".mtm",".mux",".narrative",".nkb",".nks",".nkx",".nml",".note",".nrt",".nst",".ntn",".nwc",".obw",
			".okt",".omx",".ots",".ovw",".pandora",".pca",".pek",".pna",".psm",".ptm",".pts",".rax",".rgrp",
			".rmi",".rmx",".rng",".rsn",".rso",".rti",".s3i",".sc2",".scs11",".sd2",".sfz",".sgp",".smpx",
			".sou",".sppack",".sprg",".stap",".sty",".sxt",".syn",".td0",".tta",".txw",".ult",".uni",".usf",
			".usflib",".ust",".uw",".uwf",".vap",".vc3",".vmo",".voc",".voxal",".vpm",".vpw",".vrf",".vsq",
			".wfb",".wfm",".wfp",".wow",".wproj",".wrk",".wus",".wut",".wv",".wvc",".wwu",".xmu",".xrns",
			".yookoo",".adv",".cmf",".dmc",".gmc",".mp_",".ppcx",".sbk",".sid",".sng",".sns",".vgm",".wav",
			".2sflib",".6cm",".8med",".a52",".al",".d01",".gsm",".mini2sf",".pd",".prg",".rmf",".tun",".wyz",
			".xt",".kar",".vb",".wem",".adg",".dts",".kfn",".mtf",".ncw",".pk",".dw",".vce",".ddt",".k25",
			".sf",".dvf",".aa3",".mxl",".adt",".fpa",".h5e",".mpdp",".ove",".rbs",".sd",".slx",".stx",".swa",
			".vsqx",".w01",".zpl",".mmp",".opus",".ppc",".rsf",".sdt",".xa",".xpf",".xsb",".brstm",".tak",
			".ptf",".efa",".g723",".mmf",".s3m",".sap",".vqf",".ear",".mp1",".dcm",".ay",".zvr",".pat",
			".ams",".gbs",".ics",".k26",".mp2",".mts",".myr",".psf",".ses",".shn",".snd",".wfd",".a2p",
			".a2t",".a2w",".ab",".acp",".ais",".alaw",".all",".apf",".aria",".ariax",".axa",".bwg",".c01",
			".ckf",".djr",".efe",".emy",".erb",".far",".fti",".gbproj",".gym",".h0",".h3b",".h3e",".h4e",
			".hdp",".hps",".iaa",".igr",".imp",".itls",".its",".jam",".jam",".kpl",".kt2",".l",".lof",".lqt",
			".m",".m1a",".m2",".minipsf",".minipsf2",".mogg",".mpu",".mt2",".mux",".mx3",".mx4",".mx5template",
			".npl",".ofr",".ovw",".pbf",".phy",".pjunoxl",".plst",".pno",".prg",".psf1",".psf2",".psy",".ptcop",
			".pvc",".rad",".raw",".rbs",".rcy",".rmm",".rta",".rts",".rvx",".s3z",".sd2f",".smp",".spx",".sseq",
			".ssnd",".svq",".svx",".thx",".tsp",".ub",".ulaw",".v2m",".vmf",".vtx",".wtpl",".wtpt",".xbmml",
			".xmi",".xmz",".xsp",".zgr",".atrac",".box",".hmi",".imf",".sb",".sdx",".aax",".cfa",".mxmf",
			".pac",".d00",".8svx",".ams",".nmsv",".msv",".xi",".ase",".awb",".expressionmap",".hma",".mlp",
			".mzp",".sfs",".snd",".tak",".8cm",".gm",".lvp",".alac",".avr",".bcs",".bonk",".cfxr",".dwa",
			".evr",".fda",".fff",".fzb",".gio",".gio",".gro",".jo",".jo-7z",".kin",".ksm",".ktp",".minincsf",
			".mt9",".musa",".muz",".mwand",".mws",".nap",".orc",".pmpl",".r",".record",".sdii",".seg",".snsf",
			".sth",".sti",".stw",".sw",".swav",".syn",".tfmx",".tm2",".tm8",".tmc",".ulw",".val",".voi",".wand",".xp"};
	
	public static String[] videoFileExtentions = new String[]{".gfp",".mp4.infovid",".aep",".dzp",".sfd",".mani",
			".bik",".vro",".dir",".scm",".rms",".wlmp",".playlist",".dzm",".mswmm",".3gp",".wpl",".psh",".dcr",
			".m2p",".ntp",".prproj",".trp",".aaf",".amc",".bdmv",".dck",".ivr",".m21",".mk3d",".mproj",".piv",
			".rdb",".rmp",".rv",".screenflow",".sec",".swt",".usm",".vcpf",".viewlet",".wp3",".xej",".dnc",
			".ivf",".wm",".webm",".swf",".veg",".avi",".mkv",".rec",".kmv",".vob",".msdvd",".tp",".meta",
			".3gp2",".trec",".bu",".srt",".mpeg",".wmv",".scc",".gvi",".m4v",".fbr",".ts",".aepx",".par",
			".cpi",".ism",".swi",".amx",".m2ts",".rmd",".sbk",".vpj",".mmv",".mnv",".vsp",".ifo",".mov",
			".mpg",".bin",".hdmov",".fcp",".ogm",".vc1",".vgz",".wmx",".xesc",".zm3",".bnp",".k3g",".lvix",
			".spl",".viv",".vp3",".asf",".vp6",".mob",".mp4",".flv",".wve",".vid",".pds",".dcr",".xvid",".mts",
			".dat",".rmvb",".str",".bdm",".yuv",".890",".avchd",".dmx",".m1pg",".mvd",".nvc",".roq",".tsp",
			".ttxt",".ddat",".f4f",".g64",".imovielibrary",".lsx",".proqc",".qt",".sbt",".vcr",".yog",".f4v",
			".3g2",".3mm",".ogv",".dav",".mxf",".r3d",".dzt",".smv",".mpeg4",".dxr",".dvdmedia",".fcproject",
			".ismv",".sqz",".tix",".3gpp",".clpi",".f4p",".fli",".hdv",".mvp",".rsx",".smk",".thp",".inp",
			".mvc",".video",".m15",".264",".h264",".lrv",".dv4",".mvp",".wmd",".camrec",".camproj",".divx",
			".stx",".aetx",".vep",".db2",".m2t",".mod",".dv",".sfera",".dvr",".pmf",".ced",".dash",".rm",
			".ale",".avp",".bsf",".dmsm",".dream",".imovieproj",".otrkey",".3p2",".aec",".ajp",".arcut",
			".avb",".avv",".bdt3",".bmc",".cine",".cip",".cmmp",".cmmtpl",".cmrec",".cst",".d2v",".d3v",
			".dce",".dmsd",".dmss",".dpa",".evo",".eyetv",".fbz",".flc",".flh",".fpdx",".ftc",".gcs",".gts",
			".hkm",".imoviemobile",".imovieproject",".ircp",".ismc",".izz",".izzy",".jss",".jts",".jtv",
			".kdenlive",".m21",".m2v",".mgv",".mj2",".mp21",".mpgindex",".mpls",".mpv",".mse",".mtv",".mve",
			".mxv",".ncor",".nsv",".nuv",".ogx",".pac",".photoshow",".plproj",".ppj",".prel",".prtl",".pxv",
			".qtl",".qtz",".rcd",".rum",".rvid",".rvl",".sdv",".sedprj",".seq",".sfvidcap",".siv",".smi",
			".stl",".svi",".tda3mt",".tivo",".tod",".tp0",".tpd",".tpr",".tvlayer",".tvshow",".usf",".vbc",
			".vcv",".vdo",".vdr",".vfz",".vlab",".vtt",".wcp",".wmmp",".wvx",".wxp",".xfl",".xlmv",".xml",
			".y4m",".zm1",".zm2",".lrec",".mp4v",".mys",".w32",".aqt",".cvc",".gom",".mpeg1",".mpv2",".orv",
			".rmv",".ssm",".zeg",".moi",".zmv",".wtv",".mjp",".arf",".gifv",".mpe",".dpg",".mpl",".rcproject",
			".60d",".moff",".tdt",".dvr-ms",".bmk",".tvs",".asx",".edl",".smil",".snagproj",".dv-avi",".eye",
			".mp21",".pgi",".pro",".avs",".box",".int",".irf",".mp2v",".scn",".ismclip",".avs",".evo",".smi",
			".m4e",".mpg2",".tdx",".vivo",".movie",".vf",".amv",".3gpp2",".psb",".axm",".cmproj",".dmsd3d",
			".dvx",".ezt",".ffm",".mqv",".mvy",".vp7",".xel",".aet",".anx",".avc",".avd",".awlive",".axv",
			".bdt2",".bs4",".bvr",".byu",".camv",".clk",".cmv",".cx3",".dlx",".dmb",".dmsm3d",".exo",".fbr",
			".fcarch",".ffd",".flx",".gvp",".iva",".jmv",".ktn",".m1v",".m2a",".m4u",".mjpg",".mpsub",".mvex",
			".osp",".pns",".pro4dvd",".pro5dvd",".pssd",".pva",".qtch",".qtindex",".qtm",".rp",".rts",".sml",
			".theater",".tid",".tvrecording",".vem",".vfw",".vix",".vs4",".vse",".wot",".787",".mvb",".nut",
			".pjs",".sec",".ssf",".mpl",".dif",".vft",".vmlt",".xmv",".anim",".grasp",".modd",".moov",".pvr",
			".vmlf",".am",".bix",".cel",".dsy",".gl",".ivs",".lsf",".m75",".mpf",".msh",".pmv",".rmd",".rts",
			".scm",".vdx"};
	
	public static boolean endsWithAnyOneWithin(String link, String[] extensions){
		boolean present = false;
		for(int i=0; i<extensions.length; i++){
			if(link.endsWith(extensions[i])){
				present = true;
				break;
			}
		}
		return present;
	}
	
	
	public static List<String> getUniqueBroken(List<String> brokenLinks){
		LinkedHashSet<String> brokenSet= new LinkedHashSet<String>();
		List<String> tempList = new ArrayList<String>();
		brokenSet.addAll(brokenLinks);
		tempList.addAll(brokenSet);
		
		return tempList;
	}
	
	public List<String> totalBroken = new ArrayList<String>();
	public List<List<String>> totalParentGroup = new ArrayList<List<String>>();
	
	public void createBrokenParentGroup(List<String> brokenList, List<String> parentList){
		 List<String> allBroken = brokenList;
		 List<String> allParent = parentList;
		 List<String> temp404Broken = new ArrayList<String>();
		 List<List<String>> tempP = new ArrayList<List<String>>();
		 int allBposition = 0;
			while( allBroken.size() >0){
				List<String> par = new ArrayList<String>();
				temp404Broken.add(allBposition, allBroken.get(0));
				par.add(allParent.get(0));
				for(int j=1; j<allBroken.size(); j++){
						if(allBroken.get(0).equals(allBroken.get(j))){
							par.add(allParent.get(j));
							allParent.remove(j);
							allBroken.remove(j);
							j = j-1;
						}
				}
				tempP.add(allBposition, par);
				allBroken.remove(0);
				allParent.remove(0);
				allBposition++;
			}
			this.totalBroken = temp404Broken;
			this.totalParentGroup = tempP;
	}
	public static String getCurrentTimeStamp() {
		String currentTime = null;
		java.util.Date dt = new java.util.Date();

		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		currentTime = sdf.format(dt);
		return currentTime;
	 
	}
	public static int generateOTP(){
		int otp = 10000;
		Random r = new Random(System.currentTimeMillis());
		otp = (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
		return otp;
	}
	public ScheduledFuture<?> startUpdating(int reqId, RequestData data, Execute execute){
		final Execute exe = execute;
		final int ReqId = reqId;
		ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(5);
		ScheduledFuture<?> future = poolExecutor.scheduleWithFixedDelay(new Runnable(){

			@Override
			public void run() {
				final RequestData data = exe.getRequestData();
				try {
					DatabaseHandler.updateInProgressRequestWithInterval(ReqId, data.clone());
				} catch (CloneNotSupportedException e) {
					logger.error(e);
				}
			}
			
		}, 1000, 4000, TimeUnit.MILLISECONDS);
		 return future;
	}
	public volatile Connection presentConnection;
	public ScheduledFuture<?> startInspection(){
		
		ScheduledThreadPoolExecutor ispector = new ScheduledThreadPoolExecutor(5);
		ScheduledFuture<?> future = ispector.scheduleWithFixedDelay(new Runnable(){

			@Override
			public void run() {
				presentConnection = DatabaseHandler.getConnection();
				//logger.info("CONNECTION BACK ? "+presentConnection);
			}
			
		}, 1000, 4000, TimeUnit.MILLISECONDS);
		return future;
	}
	
	public static String createUniqueJsonFileName(){
		return null;
	}
	public void executeRequestAsynchronously(String userEmail){
		
		final RequestData requestData;
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		final Execute execute;
		final String startURL;
		final String proxyHost;
		final String proxyPort;
		final int ReqId;
		final int continuousBrokenCount;
		final String email = userEmail;
		ReqId = DatabaseHandler.getInProgressRequestID(userEmail);
			//startURL = databaseHandler.getStartURL(ReqId);
		String[] proxy = DatabaseHandler.getProxy(userEmail);
    	if(proxy[2] != null){
    		proxyHost = proxy[0];
    		proxyPort = proxy[1];
    	}else{
    		proxyHost = null;//to create-fetch from user_account table, if value = "" change to null 
    		proxyPort = null;//to create-fetch from user_account table, if value = "" change to null
    	}
    	continuousBrokenCount = DatabaseHandler.getContinuousBrokenCount(userEmail);
    	requestData = (RequestData) DatabaseHandler.getRequest(ReqId);
		startURL = requestData.getStartURL();
		execute = new Execute(requestData);
			
		final ScheduledFuture<?> sf = startUpdating(ReqId, requestData, execute);//start updating db
		executorService.execute(new Runnable(){
				@Override
				public void run() {
					requestData.setInterrupted(false);
					execute.executeRequest(startURL, proxyHost, proxyPort, continuousBrokenCount, ReqId);
					sf.cancel(true);//stop
					
					try {
						DatabaseHandler.storeUserRequestDataAtEndOfRequest(ReqId,requestData,null);
						if(RequestDataMapper.interruptedMap.containsKey(ReqId)){
							RequestDataMapper.interruptedMap.remove(ReqId);
						}
						if(DatabaseHandler.getOldestPendingRequest(email) > 0){
							//set status of oldest pending req to IN Progress and update start time
							DatabaseHandler.modifyRequestToInProgress(DatabaseHandler.getOldestPendingRequest(email));
							//startInspection(email);
							executeRequestAsynchronously(email);
						}
					} catch (ConnectionUnavailableException e) {
						logger.warn("Error occured while creating database connection", e);
						ScheduledFuture<?> si = startInspection();
						while(presentConnection == null){
						}
						si.cancel(true);
						DatabaseHandler.storeUserRequestDataAtEndOfRequest(ReqId,requestData,presentConnection);
						if(RequestDataMapper.interruptedMap.containsKey(ReqId)){
							RequestDataMapper.interruptedMap.remove(ReqId);
						}
						if(DatabaseHandler.getOldestPendingRequest(email) > 0){
							//set status of oldest pending req to IN Progress and update start time
							DatabaseHandler.modifyRequestToInProgress(DatabaseHandler.getOldestPendingRequest(email));
							//startInspection(email);
							executeRequestAsynchronously(email);
						}
					}
					
				}
	     });
		executorService.shutdown();
		
 }
}
