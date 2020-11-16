
# react-native-yandex-mapkit

## Getting started

`$ yarn add react-native-yandex-mapkit`

or

`$ npm install react-native-yandex-mapkit --save`

### Automatic installation

`$ react-native link react-native-yandex-mapkit`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-yandex-mapkit` and add `RNYandexMapkit.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNYandexMapkit.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.svbutko.RNYandexMapKit.RNYandexMapKitPackage;` to the imports at the top of the file
  - Add `new RNYandexMapKitPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-yandex-mapkit'
  	project(':react-native-yandex-mapkit').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-yandex-mapkit/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-yandex-mapkit')
  	```
