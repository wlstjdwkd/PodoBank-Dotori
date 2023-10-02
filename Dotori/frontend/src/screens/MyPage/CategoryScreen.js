import React from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Image,
  FlatList,
} from "react-native";
import HeaderComponent from "../Components/HeaderScreen";
import { AntDesign, Entypo, FontAwesome } from "@expo/vector-icons";
import { useDispatch, useSelector } from "react-redux";
import { planCategoryList } from "../../apis/planapi"
import { useState } from "react";
import { useEffect } from "react";
const categorys = [
  {
    categorySeq: "1",
    categoryTitle: "옷(겨울)",
  },
  {
    categorySeq: "2",
    categoryTitle: "요가학원",
  },
  {
    categorySeq: "3",
    categoryTitle: "가구구매",
  },
  {
    categorySeq: "4",
    categoryTitle: "식자재",
  },
  {
    categorySeq: "5",
    categoryTitle: "배달",
  },
];

const randomColors = [
  "#FFD700",
  "#FFA07A",
  "#FFC0CB",
  "#90EE90",
  "#87CEEB",
  "#FFB6C1",
  "#FF1493",
  "#DDA0DD",
  "#F0E68C",
  "#00CED1",
  "#FF6347",
  "#E0FFFF",
  "#F08080",
  "#FFDAB9",
  "#D8BFD8",
];

export default function CategoryScreen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외

  const [categoryList, setCategoryList] = useState()
  
  const doPlanCategoryList = async () => {
    try{
      const response = await planCategoryList(accessToken, grantType)
      if(response.status === 200){
        setCategoryList(response.data)
        console.log('전체 카테고리 목록 가져오기 성공')
      }else{
        console.log('전체 카테고리 목록 가져오기 실패', response.status)
      }
    }catch(error){
      console.log('오류 발생 : 전체 카테고리 목록 가져오기 실패', error)
    }
  }

  const renderItem = ({ item }) => (
    <TouchableOpacity
      style={[
        styles.categoryItem,
        {
          backgroundColor:
            randomColors[Math.floor(Math.random() * randomColors.length)],
        },
      ]}
      onPress={() =>
        navigation.navigate("CategorySettingScreen", {
          categorySeq: item.categorySeq,
          categoryTitle: item.categoryTitle,
        })
      }
    >
      <Text style={styles.categoryTitle}>{item.categoryTitle}</Text>
      <Text style={styles.categoryGroup}>{item.categoryGroup}</Text>
      <Image
        style={styles.arrowIcon}
        source={require("../../assets/icon/forward_arrow.png")} // 화살표 아이콘 이미지 경로
      />
    </TouchableOpacity>
  );

  useEffect(()=>{
    doPlanCategoryList()
  }, [])

  return (
    <View style={styles.container}>
      <HeaderComponent title="카테고리 보기" />

      {/* 상단 작은 사각형 4개 */}
      <View style={styles.titleContainer}>
        <View style={styles.rectangleContainer}>
          <View style={styles.smallRectangles}>
            <View
              style={[styles.smallRectangle, { backgroundColor: "#FFEEAD" }]}
            />
            <View
              style={[styles.smallRectangle, { backgroundColor: "#D9534F" }]}
            />
          </View>
          <View style={styles.smallRectangles}>
            <View
              style={[styles.smallRectangle, { backgroundColor: "#96CEB4" }]}
            />
            <View
              style={[styles.smallRectangle, { backgroundColor: "#FFAD60" }]}
            />
          </View>
        </View>
        <Text style={styles.titleText}>카테고리 </Text>
      </View>

      {/* 카테고리 목록 */}
      <FlatList
        data={categorys}
        renderItem={renderItem}
        keyExtractor={(item) => item.categorySeq}
        contentContainerStyle={styles.categoryList}
      />
      <FlatList
        data={categoryList}
        renderItem={renderItem}
        keyExtractor={(item) => item.categorySeq}
        contentContainerStyle={styles.categoryList}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "white",
    padding: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 16,
    marginTop: 15,
  },
  arrowIcon: {
    // width: 12,
    // height: 12,
  },
  CategoryText: {
    fontSize: 24,
    flex: 1,
    textAlign: "center",
  },
  titleContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginLeft: "10%",
    marginTop: 30,
    marginBottom: 40,
  },
  smallRectangles: {
    alignItems: "center",
    justifyContent: "space-between", // 가로 간격을 균등하게 분배
    marginBottom: 10,
  },
  smallRectangle: {
    width: 12,
    height: 12,
    backgroundColor: "red", // 임시 색상
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
    margin: 1,
  },
  categoryLabel: {
    fontSize: 14,
    fontWeight: "bold",
    marginLeft: 10,
  },
  rectangleContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 10,
    marginRight: 10,
    marginTop: 10,
  },
  titleText: {
    fontSize: 20,
    marginTop: -2,
    fontWeight: "bold",
  },
  categoryList: {
    alignItems: "center", // 중앙 정렬
    width: "80%", // 화면 가로폭의 80%
    marginLeft: "auto",
    marginRight: "auto",
  },
  categoryItem: {
    flexDirection: "row",
    alignItems: "center",
    padding: 12,
    marginBottom: 15,
    borderRadius: 28,
    height: 50,
    width: "100%", // 카테고리 아이템 가로폭을 100%로 설정
  },
  categoryTitle: {
    fontSize: 16,
    marginRight: "auto",
    marginLeft: "5%",
  },
  arrowIcon: {
    width: 20,
    height: 20,
  },
});
