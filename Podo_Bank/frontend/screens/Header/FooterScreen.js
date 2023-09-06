import React from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import { AntDesign, FontAwesome5 } from "@expo/vector-icons"; // Expo에서 제공하는 아이콘 라이브러리

export default function FooterScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={styles.iconContainer}
        onPress={() => navigation.navigate("HomeScreen")}
      >
        <AntDesign name="home" size={24} color="black" />
        <Text style={styles.iconText}>홈</Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={styles.iconContainer}
        onPress={() => navigation.navigate("AccountOverviewScreen")}
      >
        <FontAwesome5 name="university" size={24} color="black" />
        <Text style={styles.iconText}>전체계좌</Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={styles.iconContainer}
        onPress={() => navigation.navigate("AccountConfigurationScreen")}
      >
        <FontAwesome5 name="file-invoice-dollar" size={24} color="black" />
        <Text style={styles.iconText}>계좌개설</Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={styles.iconContainer}
        onPress={() => navigation.navigate("")}
      >
        <FontAwesome5 name="user" size={24} color="black" />
        <Text style={styles.iconText}>마이페이지</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    justifyContent: "space-around",
    alignItems: "center",
    position: "absolute",
    bottom: 0,
    width: "100%",
    backgroundColor: "white",
    borderTopWidth: 1,
    borderColor: "#e0e0e0",
    paddingVertical: 10,
  },
  iconContainer: {
    alignItems: "center",
  },
  iconText: {
    fontSize: 15,
  },
});
