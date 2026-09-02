package com.example.ui.i18n

import com.example.data.model.AppNotification
import com.example.data.model.CropListing
import com.example.data.model.FarmerProfile
import com.example.data.model.MandiRegion
import com.example.data.model.MarketPriceUpdate
import com.example.data.model.Order
import com.example.ui.viewmodel.SortOption

object ProduceTranslator {

    fun translateCategory(category: String, language: AppLanguage): String {
        val cat = category.trim().lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> when {
                cat.contains("veg") -> "Vegetables"
                cat.contains("grain") || cat.contains("wheat") -> "Grains & Wheat"
                cat.contains("fruit") -> "Fresh Fruits"
                cat.contains("pulse") || cat.contains("dal") -> "Pulses & Dal"
                cat.contains("spice") -> "Farm Spices"
                cat.contains("org") -> "100% Organic"
                cat.contains("all") -> "All Crops"
                else -> category
            }
            AppLanguage.HINDI -> when {
                cat.contains("veg") -> "सब्जियां"
                cat.contains("grain") || cat.contains("wheat") -> "अनाज व गेहूं"
                cat.contains("fruit") -> "ताजे फल"
                cat.contains("pulse") || cat.contains("dal") -> "दालें व दलहन"
                cat.contains("spice") -> "खेत के मसाले"
                cat.contains("org") -> "100% जैविक"
                cat.contains("all") -> "सभी फसलें"
                else -> category
            }
            AppLanguage.MARATHI -> when {
                cat.contains("veg") -> "भाजीपाला"
                cat.contains("grain") || cat.contains("wheat") -> "धान्य व गहू"
                cat.contains("fruit") -> "ताजी फळे"
                cat.contains("pulse") || cat.contains("dal") -> "डाळी व कडधान्ये"
                cat.contains("spice") -> "मसाले पिके"
                cat.contains("org") -> "100% सेंद्रिय"
                cat.contains("all") -> "सर्व पिके"
                else -> category
            }
            AppLanguage.GUJARATI -> when {
                cat.contains("veg") -> "શાકભાજી"
                cat.contains("grain") || cat.contains("wheat") -> "અનાજ અને ઘઉં"
                cat.contains("fruit") -> "તાજા ફળો"
                cat.contains("pulse") || cat.contains("dal") -> "કઠોળ અને દાળ"
                cat.contains("spice") -> "મસાલા પાક"
                cat.contains("org") -> "100% જૈવિક"
                cat.contains("all") -> "બધા પાક"
                else -> category
            }
            AppLanguage.PUNJABI -> when {
                cat.contains("veg") -> "ਸਬਜ਼ੀਆਂ"
                cat.contains("grain") || cat.contains("wheat") -> "ਅਨਾਜ ਤੇ ਕਣਕ"
                cat.contains("fruit") -> "ਤਾਜ਼ੇ ਫਲ"
                cat.contains("pulse") || cat.contains("dal") -> "ਦਾਲਾਂ"
                cat.contains("spice") -> "ਮਸਾਲੇ"
                cat.contains("org") -> "100% ਜੈਵਿਕ"
                cat.contains("all") -> "ਸਾਰੀਆਂ ਫ਼ਸਲਾਂ"
                else -> category
            }
            AppLanguage.BENGALI -> when {
                cat.contains("veg") -> "শাকসবজি"
                cat.contains("grain") || cat.contains("wheat") -> "শস্য ও গম"
                cat.contains("fruit") -> "তাজা ফল"
                cat.contains("pulse") || cat.contains("dal") -> "ডাল ও কলাই"
                cat.contains("spice") -> "মসলা"
                cat.contains("org") -> "১০০% জৈব"
                cat.contains("all") -> "সকল ফসল"
                else -> category
            }
            AppLanguage.TELUGU -> when {
                cat.contains("veg") -> "కూరగాయలు"
                cat.contains("grain") || cat.contains("wheat") -> "ధాన్యాలు & గోధుమలు"
                cat.contains("fruit") -> "తాజా పండ్లు"
                cat.contains("pulse") || cat.contains("dal") -> "పప్పు దినుసులు"
                cat.contains("spice") -> "సుగంధ ద్రవ్యాలు"
                cat.contains("org") -> "100% సేంద్రీయ"
                cat.contains("all") -> "అన్ని పంటలు"
                else -> category
            }
            AppLanguage.TAMIL -> when {
                cat.contains("veg") -> "காய்கறிகள்"
                cat.contains("grain") || cat.contains("wheat") -> "தானியங்கள் & கோதுமை"
                cat.contains("fruit") -> "புதிய பழங்கள்"
                cat.contains("pulse") || cat.contains("dal") -> "பருப்பு வகைகள்"
                cat.contains("spice") -> "மசாலா பொருட்கள்"
                cat.contains("org") -> "100% இயற்கை"
                cat.contains("all") -> "அனைத்து பயிர்கள்"
                else -> category
            }
            AppLanguage.KANNADA -> when {
                cat.contains("veg") -> "ತರಕಾರಿಗಳು"
                cat.contains("grain") || cat.contains("wheat") -> "ಧಾನ್ಯಗಳು ಮತ್ತು ಗೋಧಿ"
                cat.contains("fruit") -> "ತಾಜಾ ಹಣ್ಣುಗಳು"
                cat.contains("pulse") || cat.contains("dal") -> "ಬೇಳೆಕಾಳುಗಳು"
                cat.contains("spice") -> "ಸಾಂಬಾರ ಪದಾರ್ಥಗಳು"
                cat.contains("org") -> "100% ಸಾವಯವ"
                cat.contains("all") -> "ಎಲ್ಲಾ ಬೆಳೆಗಳು"
                else -> category
            }
            AppLanguage.MALAYALAM -> when {
                cat.contains("veg") -> "പച്ചക്കറികൾ"
                cat.contains("grain") || cat.contains("wheat") -> "ധാന്യങ്ങളും ഗോതമ്പും"
                cat.contains("fruit") -> "പുതിയ പഴങ്ങൾ"
                cat.contains("pulse") || cat.contains("dal") -> "പയറുവർഗ്ഗങ്ങൾ"
                cat.contains("spice") -> "സുഗന്ധവ്യഞ്ജനങ്ങൾ"
                cat.contains("org") -> "100% ജൈവം"
                cat.contains("all") -> "എല്ലാ വിളകളും"
                else -> category
            }
            AppLanguage.ODIA -> when {
                cat.contains("veg") -> "ପନିପରିବା"
                cat.contains("grain") || cat.contains("wheat") -> "ଶସ୍ୟ ଓ ଗହମ"
                cat.contains("fruit") -> "ତାଜା ଫଳ"
                cat.contains("pulse") || cat.contains("dal") -> "ଡାଲି ଜାତୀୟ"
                cat.contains("spice") -> "ମସଲା"
                cat.contains("org") -> "୧୦୦% ଜୈବିକ"
                cat.contains("all") -> "ସମସ୍ତ ଫସଲ"
                else -> category
            }
        }
    }

    fun translateUnit(unit: String, language: AppLanguage): String {
        val u = unit.trim().lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> when {
                u == "kg" || u == "kgs" -> "kg"
                u.contains("quintal") -> "quintal"
                u.contains("crate") -> "crate"
                u.contains("bag") -> "bag"
                u.contains("ton") -> "ton"
                else -> unit
            }
            AppLanguage.HINDI -> when {
                u == "kg" || u == "kgs" -> "किग्रा"
                u.contains("quintal") -> "क्विंटल"
                u.contains("crate") -> "क्रेट"
                u.contains("bag") -> "बोरी"
                u.contains("ton") -> "टन"
                else -> unit
            }
            AppLanguage.MARATHI -> when {
                u == "kg" || u == "kgs" -> "किलो"
                u.contains("quintal") -> "क्विंटल"
                u.contains("crate") -> "क्रेट"
                u.contains("bag") -> "पोते"
                u.contains("ton") -> "टन"
                else -> unit
            }
            AppLanguage.GUJARATI -> when {
                u == "kg" || u == "kgs" -> "કિલો"
                u.contains("quintal") -> "ક્વિન્ટલ"
                u.contains("crate") -> "ક્રેટ"
                u.contains("bag") -> "ગુણી"
                u.contains("ton") -> "ટન"
                else -> unit
            }
            AppLanguage.PUNJABI -> when {
                u == "kg" || u == "kgs" -> "ਕਿਲੋ"
                u.contains("quintal") -> "ਕੁਇੰਟਲ"
                u.contains("crate") -> "ਕਰੇਟ"
                u.contains("bag") -> "ਬੋਰੀ"
                u.contains("ton") -> "ਟਨ"
                else -> unit
            }
            AppLanguage.BENGALI -> when {
                u == "kg" || u == "kgs" -> "কেজি"
                u.contains("quintal") -> "কুইন্টাল"
                u.contains("crate") -> "ক্রেট"
                u.contains("bag") -> "বস্তা"
                u.contains("ton") -> "টন"
                else -> unit
            }
            AppLanguage.TELUGU -> when {
                u == "kg" || u == "kgs" -> "కిలో"
                u.contains("quintal") -> "క్వింటాల్"
                u.contains("crate") -> "క్రేట్"
                u.contains("bag") -> "బస్తా"
                u.contains("ton") -> "టన్ను"
                else -> unit
            }
            AppLanguage.TAMIL -> when {
                u == "kg" || u == "kgs" -> "கிலோ"
                u.contains("quintal") -> "குவிண்டால்"
                u.contains("crate") -> "கூடை"
                u.contains("bag") -> "மூட்டை"
                u.contains("ton") -> "டன்"
                else -> unit
            }
            AppLanguage.KANNADA -> when {
                u == "kg" || u == "kgs" -> "ಕೆಜಿ"
                u.contains("quintal") -> "ಕ್ವಿಂಟಾಲ್"
                u.contains("crate") -> "ಕ್ರೇಟ್"
                u.contains("bag") -> "ಚೀಲ"
                u.contains("ton") -> "ಟನ್"
                else -> unit
            }
            AppLanguage.MALAYALAM -> when {
                u == "kg" || u == "kgs" -> "കിലോ"
                u.contains("quintal") -> "ക്വിന്റൽ"
                u.contains("crate") -> "ക്രേറ്റ്"
                u.contains("bag") -> "ചാക്ക്"
                u.contains("ton") -> "ടൺ"
                else -> unit
            }
            AppLanguage.ODIA -> when {
                u == "kg" || u == "kgs" -> "କିଗ୍ରା"
                u.contains("quintal") -> "କ୍ୱିଣ୍ଟାଲ"
                u.contains("crate") -> "କ୍ରେଟ୍"
                u.contains("bag") -> "ବସ୍ତା"
                u.contains("ton") -> "ଟନ୍"
                else -> unit
            }
        }
    }

    fun translateCropTitle(title: String, language: AppLanguage): String {
        val t = title.lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> when {
                t.contains("onion") -> "नासिक ताजा लाल प्याज (गरवा क्वालिटी)"
                t.contains("basmati") -> "पूसा 1121 एक्स्ट्रा लॉन्ग बासमती धान/चावल"
                t.contains("tomato") -> "पौध-पके देसी हाइब्रिड टमाटर"
                t.contains("bell pepper") || t.contains("capsicum") -> "पॉलीहाउस रंगीन शिमला मिर्च"
                t.contains("wheat") || t.contains("sharbati") -> "शरबती एमपी गोल्डन गेहूं (साफ व ग्रेडेड)"
                t.contains("alphonso") || t.contains("mango") -> "प्रामाणिक देवगढ़ / रत्नागिरी हापुस आम"
                t.contains("chilli") || t.contains("chillies") -> "गुंटूर सन्नम S4 सूखी लाल मिर्च (डंठल रहित)"
                t.contains("chana") || t.contains("chickpea") -> "देसी चना (मोटा अनपॉलिश)"
                else -> title
            }
            AppLanguage.MARATHI -> when {
                t.contains("onion") -> "नाशिक ताजा लाल कांदा (गरवा दर्जा)"
                t.contains("basmati") -> "पुसा 1121 लांब दाण्याचा बासमती भात"
                t.contains("tomato") -> "झाडावर पिकलेला देशी हायब्रिड टोमॅटो"
                t.contains("bell pepper") || t.contains("capsicum") -> "पॉलीहाऊस रंगीत ढोबळी शिमला मिरची"
                t.contains("wheat") || t.contains("sharbati") -> "शरबती एमपी सुवर्ण गहू (स्वच्छ व निवडक)"
                t.contains("alphonso") || t.contains("mango") -> "अस्सल देवगड / रत्नागिरी हापूस आंबा"
                t.contains("chilli") || t.contains("chillies") -> "गुंटूर सन्नम S4 सुकी लाल मिरची (बिन देठाची)"
                t.contains("chana") || t.contains("chickpea") -> "देशी काबुली / जाड हरभरा (अनपॉलिश)"
                else -> title
            }
            AppLanguage.GUJARATI -> when {
                t.contains("onion") -> "નાસિક તાજી લાલ ડુંગળી (ગરવા ગુણવત્તા)"
                t.contains("basmati") -> "પુસા 1121 એક્સ્ટ્રા લોંગ બાસમતી ડાંગર/ચોખા"
                t.contains("tomato") -> "વેલ પર પાકેલા દેશી હાઇબ્રિડ ટામેટાં"
                t.contains("bell pepper") || t.contains("capsicum") -> "પોલીહાઉસ રંગીન કેપ્સિકમ શિમલા મરચાં"
                t.contains("wheat") || t.contains("sharbati") -> "શરબતી એમપી સોનેરી ઘઉં (ગ્રેડેડ)"
                t.contains("alphonso") || t.contains("mango") -> "અસલ દેવગઢ / રત્નાગિરી હાફૂસ કેરી"
                t.contains("chilli") || t.contains("chillies") -> "ગુંટૂર સન્નમ S4 સૂકા લાલ મરચાં"
                t.contains("chana") || t.contains("chickpea") -> "દેશી ચણા (અનપોલિશ્ડ)"
                else -> title
            }
            AppLanguage.PUNJABI -> when {
                t.contains("onion") -> "ਨਾਸਿਕ ਤਾਜ਼ੇ ਲਾਲ ਪਿਆਜ਼ (ਗਰਵਾ ਕੁਆਲਿਟੀ)"
                t.contains("basmati") -> "ਪੂਸਾ 1121 ਐਕਸਟਰਾ ਲਾਂਗ ਬਾਸਮਤੀ ਝੋਨਾ/ਚੌਲ"
                t.contains("tomato") -> "ਤਾਜ਼ੇ ਦੇਸੀ ਹਾਈਬ੍ਰਿਡ ਟਮਾਟਰ"
                t.contains("bell pepper") || t.contains("capsicum") -> "ਪੌਲੀਹਾਊਸ ਰੰਗਦਾਰ ਸ਼ਿਮਲਾ ਮਿਰਚ"
                t.contains("wheat") || t.contains("sharbati") -> "ਸ਼ਰਬਤੀ ਐਮਪੀ ਗੋਲਡਨ ਕਣਕ (ਸਾਫ਼ ਤੇ ਗ੍ਰੇਡੇਡ)"
                t.contains("alphonso") || t.contains("mango") -> "ਅਸਲੀ ਦੇਵਗੜ੍ਹ / ਰਤਨਾਗਿਰੀ ਹਾਪੁਸ ਅੰਬ"
                t.contains("chilli") || t.contains("chillies") -> "ਗੁੰਟੂਰ ਸੰਨਮ S4 ਸੁੱਕੀਆਂ ਲਾਲ ਮਿਰਚਾਂ"
                t.contains("chana") || t.contains("chickpea") -> "ਦੇਸੀ ਮੋਟੇ ਛੋਲੇ (ਅਨਪਾਲਿਸ਼ਡ)"
                else -> title
            }
            AppLanguage.BENGALI -> when {
                t.contains("onion") -> "নাসিক তাজা লাল পেঁয়াজ (গারওয়া কোয়ালিটি)"
                t.contains("basmati") -> "পুসা ১১২১ এক্সট্রা লং বাসমতি চাল"
                t.contains("tomato") -> "গাছে পাকা দেশি হাইব্রিড টমেটো"
                t.contains("bell pepper") || t.contains("capsicum") -> "পলিহাউস রঙিন ক্যাপসিকাম"
                t.contains("wheat") || t.contains("sharbati") -> "শরবতী এমপি সোনালী গম"
                t.contains("alphonso") || t.contains("mango") -> "আসল দেবগড় / রত্নাগিরি আলফানসো আম"
                t.contains("chilli") || t.contains("chillies") -> "গুন্টুর সন্নম S4 শুকনো লাল মরিচ"
                t.contains("chana") || t.contains("chickpea") -> "দেশি গোটা ছোলা (আনপালিশ)"
                else -> title
            }
            AppLanguage.TELUGU -> when {
                t.contains("onion") -> "నాసిక్ తాజా ఎర్ర ఉల్లిపాయలు (గర్వా నాణ్యత)"
                t.contains("basmati") -> "పూసా 1121 ఎక్స్‌ట్రా లాంగ్ బాస్మతి వరి/బియ్యం"
                t.contains("tomato") -> "చెట్టుపై పండిన నాటు హైబ్రిడ్ టమాటాలు"
                t.contains("bell pepper") || t.contains("capsicum") -> "పాలీహౌస్ కలర్డ్ క్యాప్సికమ్"
                t.contains("wheat") || t.contains("sharbati") -> "శర్బతి ఎంపీ గోల్డెన్ గోధుమలు"
                t.contains("alphonso") || t.contains("mango") -> "నిజమైన దేవగఢ్ / రత్నగిరి ఆల్ఫాన్సో మామిడి"
                t.contains("chilli") || t.contains("chillies") -> "గుంటూరు సన్నం S4 ఎండు మిరపకాయలు"
                t.contains("chana") || t.contains("chickpea") -> "దేశీ శనగలు (అన్‌పాలిష్డ్)"
                else -> title
            }
            AppLanguage.TAMIL -> when {
                t.contains("onion") -> "நாசிக் புதிய சிவப்பு வெங்காயம் (கர்வா தரம்)"
                t.contains("basmati") -> "பூசா 1121 நீள பாஸ்மதி நெல்/அரிசி"
                t.contains("tomato") -> "செடியில் பழுத்த நாட்டு கலப்பின தக்காளி"
                t.contains("bell pepper") || t.contains("capsicum") -> "பாலிஹவுஸ் குடைமிளகாய்"
                t.contains("wheat") || t.contains("sharbati") -> "ஷர்பதி எம்பி தங்க கோதுமை"
                t.contains("alphonso") || t.contains("mango") -> "அசல் தேவ்காட் / ரத்னகிரி அல்போன்சா மாம்பழம்"
                t.contains("chilli") || t.contains("chillies") -> "குண்டூர் சன்னம் S4 காய்ந்த சிவப்பு மிளகாய்"
                t.contains("chana") || t.contains("chickpea") -> "நாட்டு கொண்டைக்கடலை"
                else -> title
            }
            AppLanguage.KANNADA -> when {
                t.contains("onion") -> "ನಾಸಿಕ್ ತಾಜಾ ಕೆಂಪು ಈರುಳ್ಳಿ (ಗರ್ವಾ ಗುಣಮಟ್ಟ)"
                t.contains("basmati") -> "ಪೂಸಾ 1121 ಉದ್ದನೆಯ ಬಾಸ್ಮತಿ ಭತ್ತ/ಅಕ್ಕಿ"
                t.contains("tomato") -> "ಗಿಡದಲ್ಲೇ ಹಣ್ಣಾದ ನಾಟಿ ಹೈಬ್ರಿಡ್ ಟೊಮೆಟೊ"
                t.contains("bell pepper") || t.contains("capsicum") -> "ಪಾಲಿಹೌಸ್ ಬಣ್ಣದ ಕ್ಯಾಪ್ಸಿಕಂ"
                t.contains("wheat") || t.contains("sharbati") -> "ಶರ್ಬತಿ ಎಂಪಿ ಗೋಲ್ಡನ್ ಗೋಧಿ"
                t.contains("alphonso") || t.contains("mango") -> "ಅಪ್ಪಟ ದೇವಗಡ / ರತ್ನಗಿರಿ ಆಪೂಸ್ ಮಾವಿನಹಣ್ಣು"
                t.contains("chilli") || t.contains("chillies") -> "ಗುಂಟೂರು ಸನ್ನಂ S4 ಒಣ ಕೆಂಪು ಮೆಣಸಿನಕಾಯಿ"
                t.contains("chana") || t.contains("chickpea") -> "ದೇಶಿ ಕಡಲೆಕಾಳು (ಪಾಲಿಶ್ ಮಾಡದ)"
                else -> title
            }
            AppLanguage.MALAYALAM -> when {
                t.contains("onion") -> "നാസിക് പുതിയ ചുവന്ന ഉള്ളി (ഗർവ ഗുണനിലവാരം)"
                t.contains("basmati") -> "പൂസ 1121 നീളമുള്ള ബസ്മതി നെല്ല്/അരി"
                t.contains("tomato") -> "തോട്ടത്തിൽ പഴുത്ത നാടൻ ഹൈബ്രിഡ് തക്കാളി"
                t.contains("bell pepper") || t.contains("capsicum") -> "പോളിഹൗസ് കളർ ക്യാപ്സിക്കം"
                t.contains("wheat") || t.contains("sharbati") -> "ഷർബതി എംപി ഗോൾഡൻ ഗോതമ്പ്"
                t.contains("alphonso") || t.contains("mango") -> "യഥാർത്ഥ രത്നഗിരി അൽഫോൺസോ മാമ്പഴം"
                t.contains("chilli") || t.contains("chillies") -> "ഗുണ്ടൂർ സന്നം S4 വറ്റൽ മുളക്"
                t.contains("chana") || t.contains("chickpea") -> "നാടൻ കടല (പോളിഷ് ചെയ്യാത്തത്)"
                else -> title
            }
            AppLanguage.ODIA -> when {
                t.contains("onion") -> "ନାସିକ ତାଜା ନାଲି ପିଆଜ (ଗରୱା ଗୁଣବତ୍ତା)"
                t.contains("basmati") -> "ପୁସା ୧୧୨୧ ଲମ୍ବା ବାସମତୀ ଧାନ/ଚାଉଳ"
                t.contains("tomato") -> "ଗଛ ପାଚିଲା ଦେଶୀ ହାଇବ୍ରିଡ୍ ବିଲାତି ବାଇଗଣ/ଟମାଟୋ"
                t.contains("bell pepper") || t.contains("capsicum") -> "ପଲିହାଉସ୍ ରଙ୍ଗୀନ ଶିମଳା ଲଙ୍କା"
                t.contains("wheat") || t.contains("sharbati") -> "ଶରବତୀ ଏମପି ସୁନେଲି ଗହମ"
                t.contains("alphonso") || t.contains("mango") -> "ଅସଲି ଦେବଗଡ଼ / ରତ୍ନଗିରି ଆଲଫାନସୋ ଆମ୍ବ"
                t.contains("chilli") || t.contains("chillies") -> "ଗୁଣ୍ଟୁର ସନ୍ନମ S4 ଶୁଖିଲା ନାଲି ଲଙ୍କା"
                t.contains("chana") || t.contains("chickpea") -> "ଦେଶୀ ବୁଟ / ଚଣା"
                else -> title
            }
        }
    }

    fun translateVariety(variety: String, language: AppLanguage): String {
        val v = variety.lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> variety
            AppLanguage.HINDI -> when {
                v.contains("garwa") -> "नासिक लाल गरवा"
                v.contains("1121") -> "पूसा 1121 बासमती"
                v.contains("abhinav") -> "अभिनव देसी हाइब्रिड"
                v.contains("wonder") || v.contains("yellow") -> "रेड व येलो वंडर"
                v.contains("sharbati") || v.contains("306") -> "C-306 शरबती"
                v.contains("hapus") || v.contains("alphonso") -> "हापुस (अल्फांसो)"
                v.contains("guntur") || v.contains("s4") -> "गुंटूर S4 तीखी"
                v.contains("bold chana") || v.contains("desi") -> "देसी मोटा चना"
                v.contains("medium-large") -> "मध्यम-बड़ा (55mm+)"
                v.contains("thomson") -> "एक्सपोर्ट ग्रेड सफेद"
                v.contains("bhagwa") || v.contains("sindhuri") -> "सुपर सिंदूरी लाल"
                v.contains("maldandi") -> "मालदांडी सफेद बोल्ड"
                v.contains("polyhouse") -> "पॉलीहाउस ग्रेड"
                v.contains("86032") -> "उच्च शर्करा रिकवरी"
                v.contains("apple") || v.contains("fancy") -> "एक्स्ट्रा फैंसी लाल"
                else -> variety
            }
            AppLanguage.MARATHI -> when {
                v.contains("garwa") -> "नाशिक लाल गरवा"
                v.contains("1121") -> "पुसा 1121 बासमती"
                v.contains("abhinav") -> "अभिनव देशी हायब्रिड"
                v.contains("wonder") || v.contains("yellow") -> "लाल आणि पिवळी वंडर"
                v.contains("sharbati") || v.contains("306") -> "C-306 शरबती"
                v.contains("hapus") || v.contains("alphonso") -> "हापूस (अल्फांसो)"
                v.contains("guntur") || v.contains("s4") -> "गुंटूर S4 तिखट"
                v.contains("bold chana") || v.contains("desi") -> "देशी जाड हरभरा"
                v.contains("medium-large") -> "मध्यम-मोठा (55mm+)"
                v.contains("thomson") -> "निर्यात दर्जा पांढरी द्राक्षे"
                v.contains("bhagwa") || v.contains("sindhuri") -> "सुपर भगवा लाल"
                v.contains("maldandi") -> "मालदांडी पांढरी"
                v.contains("polyhouse") -> "पॉलीहाऊस दर्जा"
                v.contains("86032") -> "को 86032 गोडवा"
                v.contains("apple") || v.contains("fancy") -> "उत्कृष्ट लाल सफरचंद"
                else -> variety
            }
            AppLanguage.GUJARATI -> when {
                v.contains("garwa") -> "નાસિક લાલ ગરવા"
                v.contains("1121") -> "પુસા 1121 બાસમતી"
                v.contains("abhinav") -> "અભિનવ દેશી હાઇબ્રિડ"
                v.contains("sharbati") -> "C-306 શરબતી"
                v.contains("hapus") || v.contains("alphonso") -> "હાફૂસ (આલ્ફાન્સો)"
                v.contains("guntur") -> "ગુંટૂર S4 તીખી"
                v.contains("chana") -> "દેશી મોટા ચણા"
                else -> variety
            }
            AppLanguage.PUNJABI -> when {
                v.contains("garwa") -> "ਨਾਸਿਕ ਲਾਲ ਗਰਵਾ"
                v.contains("1121") -> "ਪੂਸਾ 1121 ਬਾਸਮਤੀ"
                v.contains("abhinav") -> "ਅਭਿਨਵ ਦੇਸੀ ਹਾਈਬ੍ਰਿਡ"
                v.contains("sharbati") -> "C-306 ਸ਼ਰਬਤੀ"
                v.contains("hapus") -> "ਹਾਪੁਸ ਅੰਬ"
                v.contains("guntur") -> "ਗੁੰਟੂਰ S4 ਤੇਜ਼"
                v.contains("chana") -> "ਦੇਸੀ ਮੋਟੇ ਛੋਲੇ"
                else -> variety
            }
            AppLanguage.BENGALI -> when {
                v.contains("garwa") -> "নাসিক লাল গারওয়া"
                v.contains("1121") -> "পুসা ১১২১ বাসমতি"
                v.contains("sharbati") -> "শরবতী C-৩০৬"
                v.contains("hapus") -> "হাফুস আলফানসো"
                v.contains("chana") -> "দেশি বড় ছোলা"
                else -> variety
            }
            AppLanguage.TELUGU -> when {
                v.contains("garwa") -> "నాసిక్ ఎర్ర గర్వా"
                v.contains("1121") -> "పూసా 1121 బాస్మతి"
                v.contains("sharbati") -> "శర్బతి C-306"
                v.contains("hapus") -> "ఆల్ఫాన్సో హాపుస్"
                v.contains("chana") -> "దేశీ శనగలు"
                else -> variety
            }
            AppLanguage.TAMIL -> when {
                v.contains("garwa") -> "நாசிக் கர்வா"
                v.contains("1121") -> "பூசா 1121 பாஸ்மதி"
                v.contains("sharbati") -> "ஷர்பதி C-306"
                v.contains("hapus") -> "அல்போன்சா ஹாபூஸ்"
                v.contains("chana") -> "நாட்டு கடலை"
                else -> variety
            }
            AppLanguage.KANNADA -> when {
                v.contains("garwa") -> "ನಾಸಿಕ್ ಗರ್ವಾ"
                v.contains("1121") -> "ಪೂಸಾ 1121 ಬಾಸ್ಮತಿ"
                v.contains("sharbati") -> "ಶರ್ಬತಿ C-306"
                v.contains("hapus") -> "ಆಪೂಸ್ ಆಲ್ಫಾನ್ಸೋ"
                v.contains("chana") -> "ದೇಶಿ ಕಡಲೆ"
                else -> variety
            }
            AppLanguage.MALAYALAM -> when {
                v.contains("garwa") -> "നാസിക് ഗർവ"
                v.contains("1121") -> "പൂസ 1121 ബസ്മതി"
                v.contains("sharbati") -> "ഷർബതി C-306"
                v.contains("hapus") -> "അൽഫോൺസോ"
                v.contains("chana") -> "നാടൻ കടല"
                else -> variety
            }
            AppLanguage.ODIA -> when {
                v.contains("garwa") -> "ନାସିକ ଗରୱା"
                v.contains("1121") -> "ପୁସା ୧୧୨୧ ବାସମତୀ"
                v.contains("sharbati") -> "ଶରବତୀ C-୩୦୬"
                v.contains("hapus") -> "ଆଲଫାନସୋ ହାପୁସ"
                v.contains("chana") -> "ଦେଶୀ ଚଣା"
                else -> variety
            }
        }
    }

    fun translateCommodity(commodity: String, language: AppLanguage): String {
        val c = commodity.lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> commodity
            AppLanguage.HINDI -> when {
                c.contains("onion") -> "प्याज (लाल गरवा)"
                c.contains("tomato") -> "टमाटर (हाइब्रिड)"
                c.contains("grapes") -> "अंगूर (थॉमसन)"
                c.contains("pomegranate") -> "अनार (भगवा)"
                c.contains("wheat") -> "गेहूं (शरबती / लोकवन)"
                c.contains("jowar") || c.contains("sorghum") -> "ज्वार (मालदांडी)"
                c.contains("capsicum") || c.contains("bell pepper") -> "शिमला मिर्च (हरी व रंगीन)"
                c.contains("sugarcane") -> "गन्ना (Co 86032)"
                c.contains("apple") -> "सेब (कश्मीरी रॉयल)"
                c.contains("potato") -> "आलू (ज्योति / चिप्सोना)"
                c.contains("soybean") -> "सोयाबीन (पीला JS-335)"
                c.contains("garlic") -> "लहसुन (मंदसौर)"
                c.contains("mustard") -> "सरसों (पीली / काली)"
                c.contains("chilli") -> "लाल मिर्च (तेजा / S17)"
                c.contains("cotton") -> "कपास (मध्यम स्टेपल)"
                c.contains("turmeric") -> "हल्दी (कच्ची गांठ)"
                c.contains("paddy") || c.contains("rice") -> "धान (बासमती पूसा 1121)"
                c.contains("orange") -> "संतरा (नागपुर संतरा)"
                c.contains("chana") || c.contains("gram") -> "चना (देसी चना)"
                c.contains("ragi") || c.contains("millet") -> "रागी (मंडुआ)"
                c.contains("coconut") -> "नारियल (तिप्तूर)"
                c.contains("groundnut") -> "मूंगफली (बोल्ड दाना)"
                c.contains("cumin") || c.contains("jeera") -> "जीरा (ऊंझा)"
                c.contains("castor") -> "अरंडी (दिवेला)"
                c.contains("bajra") -> "बाजरा (मोती बाजरा)"
                c.contains("guar") -> "ग्वार बीज"
                c.contains("mango") -> "आम (दशहरी / लंगड़ा)"
                c.contains("banana") -> "केला (ग्रैंड नैन)"
                c.contains("tur") || c.contains("arhar") -> "अरहर / तुअर दाल"
                c.contains("kinnow") -> "किन्नू (पंजाब स्वीट)"
                else -> commodity
            }
            AppLanguage.MARATHI -> when {
                c.contains("onion") -> "कांदा (लाल गरवा)"
                c.contains("tomato") -> "टोमॅटो (हायब्रिड)"
                c.contains("grapes") -> "द्राक्षे (थॉमसन)"
                c.contains("pomegranate") -> "डाळिंब (भगवा)"
                c.contains("wheat") -> "गहू (शरबती / लोकवन)"
                c.contains("jowar") || c.contains("sorghum") -> "ज्वारी (मालदांडी)"
                c.contains("capsicum") || c.contains("bell pepper") -> "ढोबळी मिरची (रंगीत)"
                c.contains("sugarcane") -> "ऊस (को 86032)"
                c.contains("apple") -> "सफरचंद (काश्मिरी रॉयल)"
                c.contains("potato") -> "बटाटा (ज्योती / चिप्सोना)"
                c.contains("soybean") -> "सोयाबीन (पिवळा JS-335)"
                c.contains("garlic") -> "लसूण (मंदसौर)"
                c.contains("mustard") -> "मोहरी (पिवळी / काळी)"
                c.contains("chilli") -> "मिरची (तेजा / S17)"
                c.contains("cotton") -> "कापूस (मध्यम धागा)"
                c.contains("turmeric") -> "हळद (सांगली राजापुरी)"
                c.contains("paddy") || c.contains("rice") -> "भात / धान (बासमती)"
                c.contains("orange") -> "संत्री (नागपूर संत्रा)"
                c.contains("chana") || c.contains("gram") -> "हरभरा (देशी)"
                c.contains("ragi") || c.contains("millet") -> "नाचणी / रागी"
                c.contains("coconut") -> "नारळ"
                c.contains("groundnut") -> "भुईमूग शेंगा"
                c.contains("cumin") || c.contains("jeera") -> "जिरे (उंझा)"
                c.contains("castor") -> "एरंडी (एरंड)"
                c.contains("bajra") -> "बाजरी (हायब्रिड)"
                c.contains("guar") -> "ग्वार बी"
                c.contains("mango") -> "आंबा (हापूस / दशहरी)"
                c.contains("banana") -> "केळी (जी-९)"
                c.contains("tur") || c.contains("arhar") -> "तूर / तूर डाळ"
                c.contains("kinnow") -> "किन्नू संत्रा"
                else -> commodity
            }
            AppLanguage.GUJARATI -> when {
                c.contains("onion") -> "ડુંગળી (લાલ ગરવા)"
                c.contains("tomato") -> "ટામેટાં (હાઇબ્રિડ)"
                c.contains("grapes") -> "દ્રાક્ષ (થોમસન)"
                c.contains("pomegranate") -> "દાડમ (ભગવા)"
                c.contains("wheat") -> "ઘઉં (શરબતી / લોકવન)"
                c.contains("jowar") -> "જુવાર (માલદાંડી)"
                c.contains("capsicum") -> "કેપ્સિકમ શિમલા મરચાં"
                c.contains("sugarcane") -> "શેરડી"
                c.contains("apple") -> "સફરજન"
                c.contains("potato") -> "બટાકા"
                c.contains("soybean") -> "સોયાબીન"
                c.contains("garlic") -> "લસણ"
                c.contains("mustard") -> "રાયડો / સરસવ"
                c.contains("chilli") -> "મરચાં (લાલ / લીલા)"
                c.contains("cotton") -> "કપાસ (શંકર-૬)"
                c.contains("turmeric") -> "હળદર"
                c.contains("paddy") || c.contains("rice") -> "ડાંગર / ચોખા"
                c.contains("orange") -> "સંતરા"
                c.contains("chana") || c.contains("gram") -> "ચણા (દેશી)"
                c.contains("groundnut") -> "મગફળી (ઝીણી / જાડી)"
                c.contains("cumin") || c.contains("jeera") -> "જીરું (ઊંઝા)"
                c.contains("castor") -> "દિવેલા (એરંડા)"
                c.contains("bajra") -> "બાજરી"
                c.contains("mango") -> "કેરી (કેસર / હાફૂસ)"
                c.contains("banana") -> "કેળાં"
                else -> commodity
            }
            AppLanguage.PUNJABI -> when {
                c.contains("onion") -> "ਪਿਆਜ਼ (ਲਾਲ ਗਰਵਾ)"
                c.contains("tomato") -> "ਟਮਾਟਰ (ਹਾਈਬ੍ਰਿਡ)"
                c.contains("grapes") -> "ਅੰਗੂਰ"
                c.contains("pomegranate") -> "ਅਨਾਰ"
                c.contains("wheat") -> "ਕਣਕ (ਸ਼ਰਬਤੀ)"
                c.contains("capsicum") -> "ਸ਼ਿਮਲਾ ਮਿਰਚ"
                c.contains("sugarcane") -> "ਗੰਨਾ"
                c.contains("apple") -> "ਸੇਬ (ਕਸ਼ਮੀਰੀ)"
                c.contains("potato") -> "ਆਲੂ"
                c.contains("soybean") -> "ਸੋਇਆਬੀਨ"
                c.contains("garlic") -> "ਲਸਣ"
                c.contains("mustard") -> "ਸਰ੍ਹੋਂ (ਪੀਲੀ/ਕਾਲੀ)"
                c.contains("chilli") -> "ਲਾਲ ਮਿਰਚ"
                c.contains("cotton") -> "ਨਰਮਾ / ਕਪਾਹ"
                c.contains("paddy") || c.contains("rice") -> "ਝੋਨਾ / ਬਾਸਮਤੀ ਚੌਲ"
                c.contains("kinnow") -> "ਕਿੰਨੂ"
                c.contains("chana") || c.contains("gram") -> "ਛੋਲੇ"
                else -> commodity
            }
            AppLanguage.BENGALI -> when {
                c.contains("onion") -> "পেঁয়াজ (লাল)"
                c.contains("tomato") -> "টমেটো (হাইব্রিড)"
                c.contains("grapes") -> "আঙ্গুর"
                c.contains("pomegranate") -> "বেদানা / ডালিম"
                c.contains("wheat") -> "গম (শরবতী)"
                c.contains("capsicum") -> "ক্যাপসিকাম"
                c.contains("sugarcane") -> "আখ"
                c.contains("apple") -> "আপেল"
                c.contains("potato") -> "আলু (জ্যোতি)"
                c.contains("mustard") -> "সরিষা"
                c.contains("chilli") -> "মরিচ"
                c.contains("paddy") || c.contains("rice") -> "ধান / বাসমতি চাল"
                c.contains("mango") -> "আম (ল্যাংড়া/দশেহরি)"
                c.contains("banana") -> "কলা"
                c.contains("jute") -> "পাট"
                else -> commodity
            }
            AppLanguage.TELUGU -> when {
                c.contains("onion") -> "ఉల్లిపాయ (ఎరుపు)"
                c.contains("tomato") -> "టమాటో (హైబ్రిడ్)"
                c.contains("grapes") -> "ద్రాక్ష"
                c.contains("pomegranate") -> "దానిమ్మ"
                c.contains("wheat") -> "గోధుమలు"
                c.contains("capsicum") -> "క్యాప్సికమ్"
                c.contains("sugarcane") -> "చెరకు"
                c.contains("apple") -> "యాపిల్"
                c.contains("potato") -> "బంగాళాదుంప"
                c.contains("chilli") -> "ఎండు మిర్చి"
                c.contains("cotton") -> "పత్తి"
                c.contains("turmeric") -> "పసుపు"
                c.contains("paddy") || c.contains("rice") -> "వరి / బియ్యం"
                c.contains("mango") -> "మామిడి"
                c.contains("banana") -> "అరటి"
                c.contains("groundnut") -> "వేరుశనగ"
                else -> commodity
            }
            AppLanguage.TAMIL -> when {
                c.contains("onion") -> "வெங்காயம் (சிவப்பு)"
                c.contains("tomato") -> "தக்காளி (ஹைப்ரிட்)"
                c.contains("grapes") -> "திராட்சை"
                c.contains("pomegranate") -> "மாதுளை"
                c.contains("wheat") -> "கோதுமை"
                c.contains("capsicum") -> "குடைமிளகாய்"
                c.contains("sugarcane") -> "கரும்பு"
                c.contains("apple") -> "ஆப்பிள்"
                c.contains("potato") -> "உருளைக்கிழங்கு"
                c.contains("chilli") -> "மிளகாய்"
                c.contains("cotton") -> "பருத்தி"
                c.contains("turmeric") -> "மஞ்சள்"
                c.contains("paddy") || c.contains("rice") -> "நெல் / அரிசி"
                c.contains("banana") -> "வாழைப்பழம்"
                c.contains("coconut") -> "தேங்காய்"
                else -> commodity
            }
            AppLanguage.KANNADA -> when {
                c.contains("onion") -> "ಈರುಳ್ಳಿ (ಕೆಂಪು)"
                c.contains("tomato") -> "ಟೊಮೆಟೊ (ಹೈಬ್ರಿಡ್)"
                c.contains("grapes") -> "ದ್ರಾಕ್ಷಿ"
                c.contains("pomegranate") -> "ದಾಳಿಂಬೆ"
                c.contains("wheat") -> "ಗೋಧಿ"
                c.contains("capsicum") -> "ಕ್ಯಾಪ್ಸಿಕಂ"
                c.contains("sugarcane") -> "ಕಬ್ಬು"
                c.contains("apple") -> "ಸೇಬು"
                c.contains("potato") -> "ಆಲೂಗಡ್ಡೆ"
                c.contains("chilli") -> "ಮೆಣಸಿನಕಾಯಿ"
                c.contains("cotton") -> "ಹತ್ತಿ"
                c.contains("turmeric") -> "ಅರಿಶಿನ"
                c.contains("paddy") || c.contains("rice") -> "ಭತ್ತ / ಅಕ್ಕಿ"
                c.contains("ragi") -> "ರಾಗಿ"
                c.contains("coconut") -> "ತೆಂಗಿನಕಾಯಿ"
                else -> commodity
            }
            AppLanguage.MALAYALAM -> when {
                c.contains("onion") -> "സവാള / ഉള്ളി"
                c.contains("tomato") -> "തക്കാളി"
                c.contains("grapes") -> "മുന്തിരി"
                c.contains("pomegranate") -> "മാതളനാരങ്ങ"
                c.contains("wheat") -> "ഗോതമ്പ്"
                c.contains("capsicum") -> "ക്യാപ്സിക്കം"
                c.contains("apple") -> "ആപ്പിൾ"
                c.contains("potato") -> "ഉരുളക്കിഴങ്ങ്"
                c.contains("chilli") -> "മുളക്"
                c.contains("paddy") || c.contains("rice") -> "നെല്ല് / അരി"
                c.contains("banana") -> "ഏത്തപ്പഴം / വാഴപ്പഴം"
                c.contains("coconut") -> "തേങ്ങ"
                c.contains("rubber") -> "റബ്ബർ"
                else -> commodity
            }
            AppLanguage.ODIA -> when {
                c.contains("onion") -> "ପିଆଜ"
                c.contains("tomato") -> "ଟମାଟୋ"
                c.contains("wheat") -> "ଗହମ"
                c.contains("apple") -> "ସେଓ"
                c.contains("potato") -> "ଆଳୁ"
                c.contains("chilli") -> "ଲଙ୍କା"
                c.contains("paddy") || c.contains("rice") -> "ଧାନ / ଚାଉଳ"
                c.contains("turmeric") -> "ହଳଦୀ"
                c.contains("mustard") -> "ସୋରିଷ"
                else -> commodity
            }
        }
    }

    fun translateMandiName(mandi: String, language: AppLanguage): String {
        val m = mandi.lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> mandi
            AppLanguage.HINDI -> when {
                m.contains("nashik") -> "नासिक APMC मंडी"
                m.contains("pune") -> "पुणे जिला अनाज हब"
                m.contains("azadpur") -> "आजादपुर APMC मेगा मंडी"
                m.contains("indore") -> "इंदौर देवी अहिल्या मंडी"
                m.contains("ludhiana") -> "लुधियाना अनाज मंडी"
                m.contains("guntur") -> "गुंटूर मिर्ची यार्ड"
                m.contains("nagpur") -> "नागपुर कपास व संतरा मंडी"
                m.contains("yeshwanthpur") || m.contains("bengaluru") -> "यशवंतपुर APMC यार्ड (बेंगलुरु)"
                m.contains("sardar patel") || m.contains("ahmedabad") -> "सरदार पटेल APMC मंडी (अहमदाबाद)"
                m.contains("muhana") || m.contains("jaipur") -> "मुहाना मंडी टर्मिनल (जयपुर)"
                m.contains("lucknow") -> "नवीन गल्ला मंडी (लखनऊ)"
                m.contains("bowenpally") || m.contains("hyderabad") -> "बोवेनपल्ली कृषि बाजार (हैदराबाद)"
                m.contains("koley") || m.contains("kolkata") -> "कोले मार्केट होलसेल हब (कोलकाता)"
                m.contains("patna") -> "बाजार समिति APMC मंडी (पटना)"
                m.contains("koyambedu") || m.contains("chennai") -> "कोयम्बेडु थोक बाजार (चेन्नई)"
                m.contains("chandigarh") -> "सेक्टर 26 अनाज व सब्जी मंडी (चंडीगढ़)"
                else -> mandi
            }
            AppLanguage.MARATHI -> when {
                m.contains("nashik") -> "नाशिक APMC बाजार समिती"
                m.contains("pune") -> "पुणे जिल्हा धान्य केंद्र"
                m.contains("azadpur") -> "आझादपूर APMC महामंडी (दिल्ली)"
                m.contains("indore") -> "इंदूर देवी अहिल्या मंडी"
                m.contains("ludhiana") -> "लुधियाना धान्य मंडी"
                m.contains("guntur") -> "गुंटूर मिरची मार्केट यार्ड"
                m.contains("nagpur") -> "नागपूर कापूस व संत्रा बाजार समिती"
                m.contains("yeshwanthpur") || m.contains("bengaluru") -> "यशवंतपूर APMC यार्ड (बंगळुरू)"
                m.contains("sardar patel") || m.contains("ahmedabad") -> "सरदार पटेल APMC बाजार समिती (अहमदाबाद)"
                m.contains("muhana") || m.contains("jaipur") -> "मुहाना मार्केट टर्मिनल (जयपूर)"
                m.contains("lucknow") -> "नवीन गल्ला मंडी (लखनौ)"
                m.contains("bowenpally") || m.contains("hyderabad") -> "बोवेनपल्ली कृषी बाजार (हैद्राबाद)"
                m.contains("koley") || m.contains("kolkata") -> "कोले मार्केट घाऊक केंद्र (कोलकाता)"
                m.contains("patna") -> "बाजार समिती APMC (पाटणा)"
                m.contains("koyambedu") || m.contains("chennai") -> "कोयंबेडू घाऊक बाजार (चेन्नई)"
                m.contains("chandigarh") -> "सेक्टर 26 धान्य व भाजीपाला मार्केट"
                else -> mandi
            }
            AppLanguage.GUJARATI -> when {
                m.contains("nashik") -> "નાસિક APMC મંડી"
                m.contains("pune") -> "પુણે અનાજ હબ"
                m.contains("azadpur") -> "આઝાદપુર APMC મેગા મંડી"
                m.contains("indore") -> "ઇન્દોર દેવી અહલ્યા મંડી"
                m.contains("ludhiana") -> "લુધિયાણા અનાજ મંડી"
                m.contains("guntur") -> "ગુંટૂર મરચાં યાર્ડ"
                m.contains("nagpur") -> "નાગપુર કપાસ અને સંતરા મંડી"
                m.contains("sardar patel") || m.contains("ahmedabad") -> "સરદાર પટેલ APMC મંડી (અમદાવાદ)"
                m.contains("muhana") || m.contains("jaipur") -> "મુહાના મંડી (જયપુર)"
                else -> mandi
            }
            AppLanguage.PUNJABI -> when {
                m.contains("ludhiana") -> "ਲੁਧਿਆਣਾ ਅਨਾਜ ਮੰਡੀ"
                m.contains("chandigarh") -> "ਸੈਕਟਰ 26 ਅਨਾਜ ਤੇ ਸਬਜ਼ੀ ਮੰਡੀ"
                m.contains("azadpur") -> "ਆਜ਼ਾਦਪੁਰ APMC ਮੈਗਾ ਮੰਡੀ"
                m.contains("nashik") -> "ਨਾਸਿਕ APMC ਮੰਡੀ"
                m.contains("indore") -> "ਇੰਦੌਰ ਦੇਵੀ ਅਹੱਲਿਆ ਮੰਡੀ"
                else -> mandi
            }
            AppLanguage.BENGALI -> when {
                m.contains("koley") || m.contains("kolkata") -> "কোলে মার্কেট পাইকারি বাজার (কলকাতা)"
                m.contains("azadpur") -> "আজাদপুর এপিএমসি মেগা মান্ডি"
                m.contains("nashik") -> "নাসিক এপিএমসি মান্ডি"
                else -> mandi
            }
            AppLanguage.TELUGU -> when {
                m.contains("guntur") -> "గుంటూరు మిర్చి యార్డ్"
                m.contains("bowenpally") || m.contains("hyderabad") -> "బోవెన్‌పల్లి వ్యవసాయ మార్కెట్ (హైదరాబాద్)"
                m.contains("nashik") -> "నాసిక్ APMC మార్కెట్"
                else -> mandi
            }
            AppLanguage.TAMIL -> when {
                m.contains("koyambedu") || m.contains("chennai") -> "கோயம்பேடு மொத்த விற்பனை சந்தை (சென்னை)"
                m.contains("nashik") -> "நாசிக் APMC சந்தை"
                else -> mandi
            }
            AppLanguage.KANNADA -> when {
                m.contains("yeshwanthpur") || m.contains("bengaluru") -> "ಯಶವಂತಪುರ APMC ಯಾರ್ಡ್ (ಬೆಂಗಳೂರು)"
                m.contains("nashik") -> "ನಾಸಿಕ್ APMC ಮಾರುಕಟ್ಟೆ"
                else -> mandi
            }
            AppLanguage.MALAYALAM -> when {
                m.contains("nashik") -> "നാസിക് APMC മാർക്കറ്റ്"
                m.contains("koyambedu") -> "കോയമ്പേട് മാർക്കറ്റ്"
                else -> mandi
            }
            AppLanguage.ODIA -> when {
                m.contains("nashik") -> "ନାସିକ APMC ମଣ୍ଡି"
                m.contains("kolkata") -> "କୋଲେ ମାର୍କେଟ୍"
                else -> mandi
            }
        }
    }

    fun translateState(state: String, language: AppLanguage): String {
        return when (language) {
            AppLanguage.ENGLISH -> state
            AppLanguage.HINDI -> when (state) {
                "Maharashtra" -> "महाराष्ट्र"
                "Punjab" -> "पंजाब"
                "Delhi" -> "दिल्ली"
                "Madhya Pradesh" -> "मध्य प्रदेश"
                "Andhra Pradesh" -> "आंध्र प्रदेश"
                "Karnataka" -> "कर्नाटक"
                "Gujarat" -> "गुजरात"
                "Rajasthan" -> "राजस्थान"
                "Uttar Pradesh" -> "उत्तर प्रदेश"
                "Telangana" -> "तेलंगाना"
                "West Bengal" -> "पश्चिम बंगाल"
                "Bihar" -> "बिहार"
                "Tamil Nadu" -> "तमिलनाडु"
                "Haryana & Punjab" -> "हरियाणा व पंजाब"
                else -> state
            }
            AppLanguage.MARATHI -> when (state) {
                "Maharashtra" -> "महाराष्ट्र"
                "Punjab" -> "पंजाब"
                "Delhi" -> "दिल्ली"
                "Madhya Pradesh" -> "मध्य प्रदेश"
                "Andhra Pradesh" -> "आंध्र प्रदेश"
                "Karnataka" -> "कर्नाटक"
                "Gujarat" -> "गुजरात"
                "Rajasthan" -> "राजस्थान"
                "Uttar Pradesh" -> "उत्तर प्रदेश"
                "Telangana" -> "तेलंगणा"
                "West Bengal" -> "पश्चिम बंगाल"
                "Bihar" -> "बिहार"
                "Tamil Nadu" -> "तामिळनाडू"
                else -> state
            }
            AppLanguage.GUJARATI -> when (state) {
                "Maharashtra" -> "મહારાષ્ટ્ર"
                "Gujarat" -> "ગુજરાત"
                "Madhya Pradesh" -> "મધ્ય પ્રદેશ"
                "Punjab" -> "પંજાબ"
                "Rajasthan" -> "રાજસ્થાન"
                else -> state
            }
            AppLanguage.PUNJABI -> when (state) {
                "Punjab" -> "ਪੰਜਾਬ"
                "Maharashtra" -> "ਮਹਾਰਾਸ਼ਟਰ"
                "Delhi" -> "ਦਿੱਲੀ"
                else -> state
            }
            AppLanguage.BENGALI -> when (state) {
                "West Bengal" -> "পশ্চিমবঙ্গ"
                "Maharashtra" -> "মহারাষ্ট্র"
                "Bihar" -> "বিহার"
                else -> state
            }
            AppLanguage.TELUGU -> when (state) {
                "Andhra Pradesh" -> "ఆంధ్రప్రదేశ్"
                "Telangana" -> "తెలంగాణ"
                "Maharashtra" -> "మహారాష్ట్ర"
                else -> state
            }
            AppLanguage.TAMIL -> when (state) {
                "Tamil Nadu" -> "தமிழ்நாடு"
                "Maharashtra" -> "மகாராஷ்டிரா"
                else -> state
            }
            AppLanguage.KANNADA -> when (state) {
                "Karnataka" -> "ಕರ್ನಾಟಕ"
                "Maharashtra" -> "ಮಹಾರಾಷ್ಟ್ರ"
                else -> state
            }
            AppLanguage.MALAYALAM -> when (state) {
                "Kerala" -> "കേരളം"
                "Tamil Nadu" -> "തമിഴ്നാട്"
                "Maharashtra" -> "മഹാരാഷ്ട്ര"
                else -> state
            }
            AppLanguage.ODIA -> when (state) {
                "Odisha" -> "ଓଡ଼ିଶା"
                "West Bengal" -> "ପଶ୍ଚିମବଙ୍ଗ"
                "Maharashtra" -> "ମହାରାଷ୍ଟ୍ର"
                else -> state
            }
        }
    }

    fun translateTrend(trend: String, language: AppLanguage): String {
        val t = trend.trim().uppercase()
        return when (language) {
            AppLanguage.ENGLISH -> when (t) {
                "UP" -> "UP"
                "DOWN" -> "DOWN"
                else -> "STABLE"
            }
            AppLanguage.HINDI -> when (t) {
                "UP" -> "तेज (बढ़ा)"
                "DOWN" -> "मंदा (गिरा)"
                else -> "स्थिर"
            }
            AppLanguage.MARATHI -> when (t) {
                "UP" -> "तेजी (वाढ)"
                "DOWN" -> "मंदी (घट)"
                else -> "स्थिर"
            }
            AppLanguage.GUJARATI -> when (t) {
                "UP" -> "તેજી (વધારો)"
                "DOWN" -> "મંદી (ઘટાડો)"
                else -> "સ્થિર"
            }
            AppLanguage.PUNJABI -> when (t) {
                "UP" -> "ਤੇਜ਼ੀ (ਵਧਿਆ)"
                "DOWN" -> "ਮੰਦੀ (ਘਟਿਆ)"
                else -> "ਸਥਿਰ"
            }
            AppLanguage.BENGALI -> when (t) {
                "UP" -> "ঊর্ধ্বমুখী"
                "DOWN" -> "নিম্নমুখী"
                else -> "স্থিতিশীল"
            }
            AppLanguage.TELUGU -> when (t) {
                "UP" -> "పెరిగింది"
                "DOWN" -> "తగ్గింది"
                else -> "స్థిరంగా"
            }
            AppLanguage.TAMIL -> when (t) {
                "UP" -> "ஏற்றம்"
                "DOWN" -> "இறக்கம்"
                else -> "நிலையானது"
            }
            AppLanguage.KANNADA -> when (t) {
                "UP" -> "ಏರಿಕೆ"
                "DOWN" -> "ಇಳಿಕೆ"
                else -> "ಸ್ಥಿರ"
            }
            AppLanguage.MALAYALAM -> when (t) {
                "UP" -> "കൂടി"
                "DOWN" -> "കുറഞ്ഞു"
                else -> "സ്ഥിരത"
            }
            AppLanguage.ODIA -> when (t) {
                "UP" -> "ବୃଦ୍ଧି"
                "DOWN" -> "ହ୍ରାସ"
                else -> "ସ୍ଥିର"
            }
        }
    }

    fun translateQualityGrade(grade: String, language: AppLanguage): String {
        val g = grade.lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> grade
            AppLanguage.HINDI -> when {
                g.contains("grade a+") -> "ग्रेड A+ (उत्कृष्ट)"
                g.contains("export") -> "निर्यात गुणवत्ता"
                g.contains("npop") || g.contains("organic") -> "जैविक NPOP प्रमाणित"
                g.contains("grade a") -> "ग्रेड A (प्रीमियम)"
                else -> grade
            }
            AppLanguage.MARATHI -> when {
                g.contains("grade a+") -> "ग्रेड A+ (उत्कृष्ट)"
                g.contains("export") -> "निर्यात दर्जा"
                g.contains("npop") || g.contains("organic") -> "सेंद्रिय NPOP प्रमाणित"
                g.contains("grade a") -> "ग्रेड A (प्रीमियम)"
                else -> grade
            }
            AppLanguage.GUJARATI -> when {
                g.contains("grade a+") -> "ગ્રેડ A+ (શ્રેષ્ઠ)"
                g.contains("export") -> "નિકાસ ગુણવત્તા"
                g.contains("organic") -> "જૈવિક પ્રમાણિત"
                else -> grade
            }
            AppLanguage.PUNJABI -> when {
                g.contains("grade a+") -> "ਗ੍ਰੇਡ A+ (ਵਧੀਆ)"
                g.contains("export") -> "ਐਕਸਪੋਰਟ ਕੁਆਲਿਟੀ"
                g.contains("organic") -> "ਜੈਵਿਕ ਪ੍ਰਮਾਣਿਤ"
                else -> grade
            }
            AppLanguage.BENGALI -> when {
                g.contains("export") -> "রপ্তানি মান"
                g.contains("organic") -> "জৈব প্রত্যয়িত"
                else -> grade
            }
            AppLanguage.TELUGU -> when {
                g.contains("export") -> "ఎగుమతి నాణ్యత"
                g.contains("organic") -> "సేంద్రీయ ధృవీకృత"
                else -> grade
            }
            AppLanguage.TAMIL -> when {
                g.contains("export") -> "ஏற்றுமதி தரம்"
                g.contains("organic") -> "இயற்கை சான்றளிக்கப்பட்டது"
                else -> grade
            }
            AppLanguage.KANNADA -> when {
                g.contains("export") -> "ರಫ್ತು ಗುಣಮಟ್ಟ"
                g.contains("organic") -> "ಸಾವಯವ ಪ್ರಮಾಣೀಕೃತ"
                else -> grade
            }
            AppLanguage.MALAYALAM -> when {
                g.contains("export") -> "കയറ്റുമതി നിലവാരം"
                g.contains("organic") -> "ജൈവ സാക്ഷ്യപ്പെടുത്തിയത്"
                else -> grade
            }
            AppLanguage.ODIA -> when {
                g.contains("export") -> "ରପ୍ତାନି ମାନ"
                g.contains("organic") -> "ଜୈବିକ ପ୍ରମାଣିତ"
                else -> grade
            }
        }
    }

    fun translateHarvestDate(date: String, language: AppLanguage): String {
        val d = date.lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> date
            AppLanguage.HINDI -> when {
                d.contains("2 days ago") -> "2 दिन पहले कटाई"
                d.contains("current season") -> "वर्तमान सीजन फसल"
                d.contains("daily fresh") -> "प्रतिदिन ताजा तुड़ाई"
                d.contains("today morning") -> "आज सुबह तोड़ा गया"
                d.contains("dry stored") -> "सुरक्षित भंडारित फसल"
                d.contains("fresh orchard") -> "ताजा बागान तुड़ाई"
                d.contains("sun dried") -> "धूप में सुखाई गई"
                d.contains("rabi") -> "ताजा रबी फसल"
                else -> date
            }
            AppLanguage.MARATHI -> when {
                d.contains("2 days ago") -> "२ दिवसांपूर्वी काढणी"
                d.contains("current season") -> "चालू हंगामातील पीक"
                d.contains("daily fresh") -> "दररोज ताजी तोडणी"
                d.contains("today morning") -> "आज सकाळी तोडलेला माल"
                d.contains("dry stored") -> "कोरड्या गोदामातील पीक"
                d.contains("fresh orchard") -> "ताजी बागेतील काढणी"
                d.contains("sun dried") -> "उन्हात वाळवलेला"
                d.contains("rabi") -> "ताजी रब्बी हंगाम काढणी"
                else -> date
            }
            AppLanguage.GUJARATI -> when {
                d.contains("2 days ago") -> "૨ દિવસ પહેલાં લણણી"
                d.contains("today morning") -> "આજે સવારે તોડેલ"
                d.contains("daily fresh") -> "રોજિંદી તાજી લણણી"
                else -> date
            }
            AppLanguage.PUNJABI -> when {
                d.contains("2 days ago") -> "੨ ਦਿਨ ਪਹਿਲਾਂ ਵਾਢੀ"
                d.contains("today morning") -> "ਅੱਜ ਸਵੇਰੇ ਤੋੜਿਆ"
                else -> date
            }
            AppLanguage.BENGALI -> when {
                d.contains("2 days ago") -> "২ দিন আগে তোলা"
                d.contains("today morning") -> "আজ সকালে তোলা"
                else -> date
            }
            AppLanguage.TELUGU -> when {
                d.contains("2 days ago") -> "2 రోజుల క్రితం కోత"
                d.contains("today morning") -> "ఈ ఉదయం కోసినది"
                else -> date
            }
            AppLanguage.TAMIL -> when {
                d.contains("2 days ago") -> "2 நாட்களுக்கு முன் அறுவடை"
                d.contains("today morning") -> "இன்று காலை பறிக்கப்பட்டது"
                else -> date
            }
            AppLanguage.KANNADA -> when {
                d.contains("2 days ago") -> "2 ದಿನಗಳ ಹಿಂದೆ ಕೊಯ್ಲು"
                d.contains("today morning") -> "ಇಂದು ಬೆಳಿಗ್ಗೆ ಕೀಳಲಾಗಿದೆ"
                else -> date
            }
            AppLanguage.MALAYALAM -> when {
                d.contains("2 days ago") -> "2 ദിവസം മുൻപ് വിളവെടുത്തത്"
                d.contains("today morning") -> "ഇന്ന് രാവിലെ പറിച്ചത്"
                else -> date
            }
            AppLanguage.ODIA -> when {
                d.contains("2 days ago") -> "୨ ଦିନ ପୂର୍ବରୁ ଅମଳ"
                d.contains("today morning") -> "ଆଜି ସକାଳେ ତୋଳା"
                else -> date
            }
        }
    }

    fun translateArrivals(arrivals: String, language: AppLanguage): String {
        val a = arrivals.lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> arrivals
            AppLanguage.HINDI -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "क्विंटल").replace("quintals", "क्विंटल")
                a.contains("crate") -> arrivals.replace("Crates", "क्रेट").replace("crates", "क्रेट")
                a.contains("tonne") || a.contains("ton") -> arrivals.replace("Tonnes", "टन").replace("tonnes", "टन")
                else -> arrivals
            }
            AppLanguage.MARATHI -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "क्विंटल").replace("quintals", "क्विंटल")
                a.contains("crate") -> arrivals.replace("Crates", "क्रेट").replace("crates", "क्रेट")
                a.contains("tonne") || a.contains("ton") -> arrivals.replace("Tonnes", "टन").replace("tonnes", "टन")
                else -> arrivals
            }
            AppLanguage.GUJARATI -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "ક્વિન્ટલ").replace("quintals", "ક્વિન્ટલ")
                a.contains("crate") -> arrivals.replace("Crates", "ક્રેટ").replace("crates", "ક્રેટ")
                a.contains("tonne") || a.contains("ton") -> arrivals.replace("Tonnes", "ટન").replace("tonnes", "ટન")
                else -> arrivals
            }
            AppLanguage.PUNJABI -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "ਕੁਇੰਟਲ").replace("quintals", "ਕੁਇੰਟਲ")
                a.contains("crate") -> arrivals.replace("Crates", "ਕਰੇਟ").replace("crates", "ਕਰੇਟ")
                a.contains("tonne") || a.contains("ton") -> arrivals.replace("Tonnes", "ਟਨ").replace("tonnes", "ਟਨ")
                else -> arrivals
            }
            AppLanguage.BENGALI -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "কুইন্টাল")
                a.contains("crate") -> arrivals.replace("Crates", "ক্রেট")
                a.contains("tonne") -> arrivals.replace("Tonnes", "টন")
                else -> arrivals
            }
            AppLanguage.TELUGU -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "క్వింటాళ్ళు")
                a.contains("crate") -> arrivals.replace("Crates", "క్రేట్లు")
                a.contains("tonne") -> arrivals.replace("Tonnes", "టన్నులు")
                else -> arrivals
            }
            AppLanguage.TAMIL -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "குவிண்டால்கள்")
                a.contains("crate") -> arrivals.replace("Crates", "கூடைகள்")
                a.contains("tonne") -> arrivals.replace("Tonnes", "டன்கள்")
                else -> arrivals
            }
            AppLanguage.KANNADA -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "ಕ್ವಿಂಟಾಲ್‌ಗಳು")
                a.contains("crate") -> arrivals.replace("Crates", "ಕ್ರೇಟ್‌ಗಳು")
                a.contains("tonne") -> arrivals.replace("Tonnes", "ಟನ್‌ಗಳು")
                else -> arrivals
            }
            AppLanguage.MALAYALAM -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "ക്വിന്റൽ")
                a.contains("crate") -> arrivals.replace("Crates", "ക്രേറ്റ്")
                a.contains("tonne") -> arrivals.replace("Tonnes", "ടൺ")
                else -> arrivals
            }
            AppLanguage.ODIA -> when {
                a.contains("quintal") -> arrivals.replace("Quintals", "କ୍ୱିଣ୍ଟାଲ")
                a.contains("crate") -> arrivals.replace("Crates", "କ୍ରେଟ୍")
                a.contains("tonne") -> arrivals.replace("Tonnes", "ଟନ୍")
                else -> arrivals
            }
        }
    }

    fun translateOrderStatus(status: String, language: AppLanguage): String {
        val s = status.trim().lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> when {
                s.contains("place") -> "Placed"
                s.contains("accept") -> "Accepted"
                s.contains("transit") -> "In Transit"
                s.contains("deliver") -> "Delivered"
                s.contains("all") -> "All Orders"
                else -> status
            }
            AppLanguage.HINDI -> when {
                s.contains("place") -> "लगाया गया"
                s.contains("accept") -> "स्वीकृत"
                s.contains("transit") -> "रास्ते में"
                s.contains("deliver") -> "डिलीवर हुआ"
                s.contains("all") -> "सभी ऑर्डर्स"
                else -> status
            }
            AppLanguage.MARATHI -> when {
                s.contains("place") -> "नोंदवली"
                s.contains("accept") -> "स्वीकारली"
                s.contains("transit") -> "वाहतुकीत"
                s.contains("deliver") -> "पोहोचली"
                s.contains("all") -> "सर्व ऑर्डर्स"
                else -> status
            }
            AppLanguage.GUJARATI -> when {
                s.contains("place") -> "મૂકાયેલ"
                s.contains("accept") -> "સ્વીકૃત"
                s.contains("transit") -> "રસ્તામાં"
                s.contains("deliver") -> "પહોંચાડેલ"
                s.contains("all") -> "બધા ઓર્ડર્સ"
                else -> status
            }
            AppLanguage.PUNJABI -> when {
                s.contains("place") -> "ਦਰਜ ਕੀਤਾ"
                s.contains("accept") -> "ਸਵੀਕਾਰਿਆ"
                s.contains("transit") -> "ਰਸਤੇ ਵਿੱਚ"
                s.contains("deliver") -> "ਡਿਲੀਵਰ ਹੋਇਆ"
                s.contains("all") -> "ਸਾਰੇ ਆਰਡਰ"
                else -> status
            }
            AppLanguage.BENGALI -> when {
                s.contains("place") -> "অর্ডারকৃত"
                s.contains("accept") -> "গৃহীত"
                s.contains("transit") -> "পথে রয়েছে"
                s.contains("deliver") -> "বিতরণ সম্পন্ন"
                s.contains("all") -> "সকল অর্ডার"
                else -> status
            }
            AppLanguage.TELUGU -> when {
                s.contains("place") -> "ఆర్డర్ చేయబడింది"
                s.contains("accept") -> "ఆమోదించబడింది"
                s.contains("transit") -> "రవాణాలో ఉంది"
                s.contains("deliver") -> "డెలివరీ చేయబడింది"
                s.contains("all") -> "అన్ని ఆర్డర్లు"
                else -> status
            }
            AppLanguage.TAMIL -> when {
                s.contains("place") -> "ஆர்டர் செய்யப்பட்டது"
                s.contains("accept") -> "ஏற்றுக்கொள்ளப்பட்டது"
                s.contains("transit") -> "பயணத்தில் உள்ளது"
                s.contains("deliver") -> "டெலிவரி செய்யப்பட்டது"
                s.contains("all") -> "அனைத்து ஆர்டர்கள்"
                else -> status
            }
            AppLanguage.KANNADA -> when {
                s.contains("place") -> "ಆರ್ಡರ್ ಮಾಡಲಾಗಿದೆ"
                s.contains("accept") -> "ಸ್ವೀಕರಿಸಲಾಗಿದೆ"
                s.contains("transit") -> "ದಾರಿಯಲ್ಲಿದೆ"
                s.contains("deliver") -> "ತಲುಪಿಸಲಾಗಿದೆ"
                s.contains("all") -> "ಎಲ್ಲಾ ಆರ್ಡರ್‌ಗಳು"
                else -> status
            }
            AppLanguage.MALAYALAM -> when {
                s.contains("place") -> "ഓർഡർ ചെയ്തു"
                s.contains("accept") -> "സ്വീകരിച്ചു"
                s.contains("transit") -> "വഴിയിലാണ്"
                s.contains("deliver") -> "ഡെലിവർ ചെയ്തു"
                s.contains("all") -> "എല്ലാ ഓർഡറുകളും"
                else -> status
            }
            AppLanguage.ODIA -> when {
                s.contains("place") -> "ଅର୍ଡର ହୋଇଛି"
                s.contains("accept") -> "ଗ୍ରହଣ ହୋଇଛି"
                s.contains("transit") -> "ବାଟରେ ଅଛି"
                s.contains("deliver") -> "ଡେଲିଭର ହୋଇଛି"
                s.contains("all") -> "ସମସ୍ତ ଅର୍ଡର"
                else -> status
            }
        }
    }

    fun translatePaymentMethod(method: String, language: AppLanguage): String {
        val m = method.lowercase()
        return when (language) {
            AppLanguage.ENGLISH -> method
            AppLanguage.HINDI -> when {
                m.contains("upi") || m.contains("escrow") -> "UPI / सीधा एस्क्रो सुरक्षित"
                m.contains("delivery") || m.contains("cash") -> "कैश ऑन डिलीवरी (COD)"
                m.contains("bank") || m.contains("neft") -> "बैंक ट्रांसफर (NEFT/RTGS)"
                else -> method
            }
            AppLanguage.MARATHI -> when {
                m.contains("upi") || m.contains("escrow") -> "UPI / थेट एस्क्रो सुरक्षित"
                m.contains("delivery") || m.contains("cash") -> "कॅश ऑन डिलिव्हरी (COD)"
                m.contains("bank") || m.contains("neft") -> "बँक ट्रान्सफर (NEFT/RTGS)"
                else -> method
            }
            AppLanguage.GUJARATI -> when {
                m.contains("upi") -> "UPI / એસ્ક્રો સુરક્ષિત"
                m.contains("cash") -> "કેશ ઓન ડિલિવરી"
                else -> method
            }
            AppLanguage.PUNJABI -> when {
                m.contains("upi") -> "UPI / ਐਸਕਰੋ ਸੁਰੱਖਿਅਤ"
                m.contains("cash") -> "ਕੈਸ਼ ਆਨ ਡਿਲੀਵਰੀ"
                else -> method
            }
            AppLanguage.BENGALI -> when {
                m.contains("upi") -> "UPI / এসক্রো সুরক্ষিত"
                m.contains("cash") -> "ক্যাশ অন ডেলিভারি"
                else -> method
            }
            AppLanguage.TELUGU -> when {
                m.contains("upi") -> "UPI / ఎస్క్రో సురక్షితం"
                m.contains("cash") -> "క్యాష్ ఆన్ డెలివరీ"
                else -> method
            }
            AppLanguage.TAMIL -> when {
                m.contains("upi") -> "UPI / எஸ்க்ரோ பாதுகாப்பானது"
                m.contains("cash") -> "கேஷ் ஆன் டெலிவரி"
                else -> method
            }
            AppLanguage.KANNADA -> when {
                m.contains("upi") -> "UPI / ಎಸ್ಕ್ರೋ ಸುರಕ್ಷಿತ"
                m.contains("cash") -> "ಕ್ಯಾಶ್ ಆನ್ ಡೆಲಿವರಿ"
                else -> method
            }
            AppLanguage.MALAYALAM -> when {
                m.contains("upi") -> "UPI / എസ്ക്രോ സുരക്ഷിതം"
                m.contains("cash") -> "ക്യാഷ് ഓൺ ഡെലിവറി"
                else -> method
            }
            AppLanguage.ODIA -> when {
                m.contains("upi") -> "UPI / ଏସ୍କ୍ରୋ ସୁରକ୍ଷିତ"
                m.contains("cash") -> "କ୍ୟାସ୍ ଅନ୍ ଡେଲିଭରି"
                else -> method
            }
        }
    }
}

// Extension helpers for UI models
fun CropListing.localizedTitle(lang: AppLanguage) = ProduceTranslator.translateCropTitle(title, lang)
fun CropListing.localizedCategory(lang: AppLanguage) = ProduceTranslator.translateCategory(category, lang)
fun CropListing.localizedVariety(lang: AppLanguage) = ProduceTranslator.translateVariety(variety, lang)
fun CropListing.localizedUnit(lang: AppLanguage) = ProduceTranslator.translateUnit(unit, lang)
fun CropListing.localizedQualityGrade(lang: AppLanguage) = ProduceTranslator.translateQualityGrade(qualityGrade, lang)
fun CropListing.localizedHarvestDate(lang: AppLanguage) = ProduceTranslator.translateHarvestDate(harvestDate, lang)
fun CropListing.localizedMandi(lang: AppLanguage) = ProduceTranslator.translateMandiName(mandiRegion, lang)
fun CropListing.localizedMandiRegion(lang: AppLanguage) = ProduceTranslator.translateMandiName(mandiRegion, lang)
fun CropListing.localizedDescription(lang: AppLanguage) = ProduceTranslator.translateCropTitle(description, lang)

fun MarketPriceUpdate.localizedCropName(lang: AppLanguage) = ProduceTranslator.translateCommodity(cropName, lang)
fun MarketPriceUpdate.localizedVariety(lang: AppLanguage) = ProduceTranslator.translateVariety(variety, lang)
fun MarketPriceUpdate.localizedCategory(lang: AppLanguage) = ProduceTranslator.translateCategory(category, lang)
fun MarketPriceUpdate.localizedMandi(lang: AppLanguage) = ProduceTranslator.translateMandiName(mandiRegion, lang)
fun MarketPriceUpdate.localizedMandiRegion(lang: AppLanguage) = ProduceTranslator.translateMandiName(mandiRegion, lang)
fun MarketPriceUpdate.localizedState(lang: AppLanguage) = ProduceTranslator.translateState(state, lang)
fun MarketPriceUpdate.localizedUnit(lang: AppLanguage) = ProduceTranslator.translateUnit(unit, lang)
fun MarketPriceUpdate.localizedTrend(lang: AppLanguage) = ProduceTranslator.translateTrend(trend, lang)
fun MarketPriceUpdate.localizedArrivals(lang: AppLanguage) = ProduceTranslator.translateArrivals(arrivalsQuantity, lang)

fun MandiRegion.localizedName(lang: AppLanguage) = ProduceTranslator.translateMandiName(name, lang)
fun MandiRegion.localizedDistrict(lang: AppLanguage) = ProduceTranslator.translateMandiName(district, lang)
fun MandiRegion.localizedState(lang: AppLanguage) = ProduceTranslator.translateState(state, lang)

fun localizedMandiRegion(regionName: String, lang: AppLanguage) = ProduceTranslator.translateMandiName(regionName, lang)
fun localizedDistrict(districtName: String, lang: AppLanguage) = ProduceTranslator.translateMandiName(districtName, lang)

fun Order.localizedCropName(lang: AppLanguage) = ProduceTranslator.translateCropTitle(cropName, lang)
fun Order.localizedCropTitle(lang: AppLanguage) = ProduceTranslator.translateCropTitle(cropName, lang)
fun Order.localizedVariety(lang: AppLanguage) = ProduceTranslator.translateVariety(variety, lang)
fun Order.localizedUnit(lang: AppLanguage) = ProduceTranslator.translateUnit(unit, lang)
fun Order.localizedMandiRegion(lang: AppLanguage) = ProduceTranslator.translateMandiName(mandiRegion, lang)
fun Order.localizedStatus(lang: AppLanguage) = ProduceTranslator.translateOrderStatus(status, lang)
fun Order.localizedPaymentMethod(lang: AppLanguage) = ProduceTranslator.translatePaymentMethod(paymentMethod, lang)
