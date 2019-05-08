import React, {Component} from "react";
import {StyleSheet} from "react-native";
import MapView from "..";

export default class App extends Component {
  render() {
    return (
        <MapView style={styles.container}/>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "red",
  },
});
