apply plugin: 'com.android.application'

//定义打包时间函数
def packageTime() {
    return new Date().format("yyyy-MM-dd", TimeZone.getTimeZone("UTC"))
}

android {
    compileSdkVersion Integer.parseInt(ANDROID_BUILD_COMPILE_SDK_VERSION)
    buildToolsVersion ANDROID_BUILD_TOOLS_VERSION

    signingConfigs {
        debug {

        }
        release {
            //storeFile file("../yourapp.keystore")
            //storePassword "your password"
            //keyAlias "your alias"
            //keyPassword "your password"

            //setting your signing.properties
            //first, add signing.properties to ./app/
            //second, add property STORE_FILE, STORE_PASSWORD, KEY_ALIAS, KEY_PASSWORD
        }
    }

    defaultConfig {
        applicationId "com.ecloud.android.xreap"
        minSdkVersion Integer.parseInt(MIN_SDK_VERSION)
        targetSdkVersion Integer.parseInt(ANDROID_BUILD_TARGET_SDK_VERSION)
        versionCode Integer.parseInt(VERSION_CODE)
        versionName VERSION_NAME

        // dex突破65535的限制
        multiDexEnabled true
    }
    buildTypes {
        debug {
            buildConfigField "boolean", "LOG_DEBUG", "true"//是否输出LOG信息
            buildConfigField "String", "API_HOST", "\"http://api.test.com\""//API Host
            minifyEnabled false
            zipAlignEnabled true
            shrinkResources true
            signingConfig signingConfigs.debug
        }

        release {
            buildConfigField "boolean", "LOG_DEBUG", "false"//是否输出LOG信息
            buildConfigField "String", "API_HOST", "\"http://api.release.com\""//API Host
            minifyEnabled false//混淆编译
            zipAlignEnabled true
            //移除无用的资源文件
            shrinkResources true
            signingConfig signingConfigs.release
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_7
        targetCompatibility JavaVersion.VERSION_1_7
    }

    lintOptions {
        disable 'InvalidPackage'
        abortOnError false
    }

    //修改生成的最终文件名
    applicationVariants.all { variant ->
        variant.outputs.each { output ->
            def outputFile = output.outputFile
            if (outputFile != null && outputFile.name.endsWith('.apk')) {
                File outputDirectory = new File(outputFile.parent);
                def fileName
                if (variant.buildType.name == "release") {
                    // 输出apk名称为app_v1.0.0_2015-06-15_playStore.apk
                    fileName = "app_v${defaultConfig.versionName}_${packageTime()}_${variant.productFlavors[0].name}.apk"
                } else {
                    fileName = "app_v${defaultConfig.versionName}_${packageTime()}_beta.apk"
                }
                output.outputFile = new File(outputDirectory, fileName)
            }
        }
    }

    productFlavors {
        playStore {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "playStore"]
        }
        miui {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "miui"]
        }
        wandoujia {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "wandoujia"]
        }
    }

//批量替换
//    productFlavors {
//        palyStore {}
//        miui {}
//        wandoujia {}
//    }
//
//    productFlavors.all {
//        flavor -> flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name]
//    }
}

File propFile = file('signing.properties');
if (propFile.exists()) {
    def Properties props = new Properties()
    props.load(new FileInputStream(propFile))
    if (props.containsKey('STORE_FILE') && props.containsKey('STORE_PASSWORD') &&
            props.containsKey('KEY_ALIAS') && props.containsKey('KEY_PASSWORD')) {
        android.signingConfigs.release.storeFile = file(props['STORE_FILE'])
        android.signingConfigs.release.storePassword = props['STORE_PASSWORD']
        android.signingConfigs.release.keyAlias = props['KEY_ALIAS']
        android.signingConfigs.release.keyPassword = props['KEY_PASSWORD']
    } else {
        android.buildTypes.release.signingConfig = null
    }
} else {
    android.buildTypes.release.signingConfig = null
}

dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    testCompile 'junit:junit:4.12'

 //   compile 'com.android.support:support-v4:22.0.0'
 //   compile 'com.android.support:multidex:1.0.0'
}