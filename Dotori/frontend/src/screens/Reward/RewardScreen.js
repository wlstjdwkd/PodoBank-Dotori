import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  Image,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  FlatList,
} from "react-native";
import FooterScreen from "../Components/FooterScreen";
import { useDispatch, useSelector } from "react-redux";
import { accountWholeBank } from "../../apis/accountapi";

const banks = [
  {
    bankSeq: 1,
    bankName: "포도은행",
    image: require("../../assets/images/logo_podo.png"),
  },
  {
    bankSeq: 2,
    bankName: "국민은행",
    image: require("../../assets/images/logo_podo.png"),
  },
  {
    bankSeq: 3,
    bankName: "카카오뱅크",
    image: require("../../assets/images/logo_podo.png"),
  },
  {
    bankSeq: 4,
    bankName: "신한은행",
    image: require("../../assets/images/logo_podo.png"),
  },
];

export default function RewardScreen({ navigation }) {
  // 토큰

  return <ScrollView contentContainerStyle={styles.container}></ScrollView>;
}
