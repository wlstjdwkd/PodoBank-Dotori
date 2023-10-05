import React, { useState, useRef, useEffect } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  Image,
  ActivityIndicator,
} from "react-native";
// import DraggableFlatList from "volkeno-react-native-drag-drop";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import { useIsFocused } from "@react-navigation/native";
import { planClassifyChatGpt } from "../../apis/planapi";

export default function PlanCreate4Screen({ navigation, route }) {
  // 토큰
  const grantType = useSelector((state) => state.user.grantType);
  const accessToken = useSelector((state) => state.user.accessToken);
  const refreshToken = useSelector((state) => state.user.refreshToken);
  const dispatch = useDispatch();
  // 그 외
  const { categorise, categoryGroups } = route.params.planInfo;

  const [isLoading, setIsLoading] = useState(false);

  const [planInfo, setPlanInfo] = useState(route.params.planInfo);
  console.log(planInfo);
  const isFocused = useIsFocused();

  const [categoryData, setCategoryData] = useState(null);
  //   [
  //   {
  //     categoryGroupName: "식비",
  //     categories: [
  //       { name: "점심", amount: 5000 },
  //       { name: "저녁", amount: 6000 },
  //       { name: "간식", amount: 3000 },
  //     ],
  //   },
  //   {
  //     categoryGroupName: "교통비",
  //     categories: [
  //       { name: "버스", amount: 1200 },
  //       { name: "택시", amount: 10000 },
  //     ],
  //   },
  // ]
  //   const categoryData = [
  //     {
  //       categoryGroupName: "식비",
  //       categories: [
  //         { name: "점심", amount: 5000 },
  //         { name: "저녁", amount: 6000 },
  //         { name: "간식", amount: 3000 },
  //       ],
  //     },
  //     {
  //       categoryGroupName: "교통비",
  //       categories: [
  //         { name: "버스", amount: 1200 },
  //         { name: "택시", amount: 10000 },
  //       ],
  //     },
  //     // ... 기타 카테고리 그룹들
  //   ];
  const [data, setData] = useState(categoryData);

  const doPlanClassifyChatGpt = async () => {
    setIsLoading(true);
    try {
      const payload = {
        categorise: categorise,
        categoryGroups: categoryGroups,
      };
      console.log(payload);
      const response = await planClassifyChatGpt(
        payload,
        accessToken,
        grantType
      );
      if (response.status === 200) {
        console.log("전체 계좌 리스트 불러오기 성공");
        setCategoryData(response.data);
      } else {
        console.log("전체 계좌 리스트 불러오기 실패");
      }
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(false);
    }
  };
  useEffect(() => {
    if (isFocused) {
      // do정보조회()
      doPlanClassifyChatGpt();
    }
  }, [isFocused]);
  //드래그앤 드롭.. 대실패 일단 보류
  const renderItem = ({ item, index, drag, isActive }) => {
    return (
      <TouchableOpacity
        style={{
          ...styles.categoryGroup,
          backgroundColor: isActive ? "#E0E0E0" : "transparent",
        }}
        onLongPress={drag}
      >
        <Text style={styles.inputText}>{item.categoryGroupName}</Text>
        <View style={styles.categoriesContainer}>
          {item.categories &&
            item.categories.map((category, idx) => (
              <View key={idx} style={styles.categoryBox}>
                <Text style={styles.categoryText}>
                  {category.categoryName} {formatNumber(category.targetAmount)}
                  원
                </Text>
              </View>
            ))}
        </View>
      </TouchableOpacity>
    );
  };

  //   const [data, setData] = useState(categoryData);
  //   const renderItem = ({ item, index, drag, isActive }) => {
  //     return (
  //       <TouchableOpacity
  //         style={{
  //           ...styles.categoryGroup,
  //           backgroundColor: isActive ? "#E0E0E0" : "transparent",
  //         }}
  //         onLongPress={drag}
  //       >
  //         <Text style={styles.inputText}>{item.categoryGroupName}</Text>
  //         <View style={styles.categoriesContainer}>
  //           {item.categories.map((category, idx) => (
  //             <View key={idx} style={styles.categoryBox}>
  //               <Text style={styles.categoryText}>
  //                 {category.name} {formatNumber(category.amount)}원
  //               </Text>
  //             </View>
  //           ))}
  //         </View>
  //       </TouchableOpacity>
  //     );
  //   };
  const formatNumber = (num) => {
    return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
  };
  const handleNextButton = () => {
    navigation.navigate("PlanCreate5Screen", {
      planInfo: planInfo,
      categoryData: categoryData,
    });
  };

  return (
    // {isLoading ? ():()}
    <View style={styles.container}>
      {isLoading ? (
        <View style={styles.loaderContainer}>
          <Image
            style={styles.loadingImage}
            source={require("../../assets/images/Hamster/LoadingHamster.png")}
          ></Image>
          <ActivityIndicator size="large" color="#FF965C" />
          <Text style={styles.loadingText}>다람쥐가 열심히 분류 중입니다!</Text>
          <Text style={styles.loadingText}>조금만 기다려 주세용</Text>
        </View>
      ) : (
        <View style={styles.innerContainer}>
          <HeaderComponent
            title="계획 생성(4/5)"
            cancelNavi="PlanMainScreen"
            navigation={navigation}
          ></HeaderComponent>
          <ScrollView style={styles.header}>
            <Text style={styles.title}>카테고리 분류</Text>
            <Text style={styles.subtitle}>
              카테고리 그룹과 카테고리를 확인해주세요.
            </Text>

            <View style={styles.center}>
              <Image
                style={styles.centerImage}
                source={require("../../assets/images/Hamster/PlanCreateHamster.png")}
              />
              <Text style={styles.questionText}>이 분류가 맞나요?</Text>
              <Text style={styles.moveText}>카테고리를 눌러서 옮겨보세요!</Text>
            </View>

            {categoryData &&
              categoryData.map((group, index) => (
                <View key={index} style={styles.categoryGroup}>
                  <Text style={styles.inputText}>
                    {group.categoryGroupName}
                  </Text>
                  <View style={styles.categoriesContainer}>
                    {group.categories.map((category, idx) => (
                      <View key={idx} style={styles.categoryBox}>
                        <Text style={styles.categoryText}>
                          {category.categoryName}{" "}
                          {formatNumber(category.targetAmount)}원
                        </Text>
                      </View>
                    ))}
                  </View>
                </View>
              ))}

            {/* <DraggableFlatList
          data={data}
          renderItem={renderItem}
          keyExtractor={(item, index) => `draggable-item-${index}`}
          onDragEnd={({ data }) => setCategoryData(data)}
        /> */}

            {/* <Text style={styles.inputText}>등록된 카테고리 그룹</Text>
        <View style={styles.categoriesContainer}>
          {categoryGroups.map((categoryGroup, index) => (
            <View key={index} style={styles.categoryBox}>
              <Text style={styles.categoryText}>{categoryGroup.name} </Text>
            </View>
          ))}
        </View> */}
          </ScrollView>

          <TouchableOpacity style={styles.button} onPress={handleNextButton}>
            <Text style={styles.buttonText}>다음</Text>
          </TouchableOpacity>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  innerContainer: {
    flex: 1,
    justifyContent: "space-between",
    // padding: 20,
    backgroundColor: "white",
  },
  loadingImage: {
    width: 200,
    height: 200,
    marginBottom: 30,
  },
  loaderContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  loadingText: {
    fontSize: 20,
    marginBottom: 20,
  },
  header: {
    flex: 1,
    // justifyContent: "center",
    // alignItems: "center",
    marginTop: 90,
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  subtitle: {
    color: "#7B7B7B",
    marginBottom: 40,
  },
  inputText: {
    fontSize: 16,
    marginBottom: 10,
  },
  input: {
    width: "100%",
    height: 50,
    backgroundColor: "#D9D9D920",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    marginBottom: 10,
    // textAlign: "center",
  },
  categoryText: {
    fontSize: 12,
  },
  button: {
    height: 50,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
  inputBehindText: {
    color: "#7B7B7B",
    marginBottom: 40,
    fontSize: 12,
    marginLeft: 10,
  },
  rowContainer: {
    flexDirection: "row",
  },
  addButton: {
    flex: 1,
    backgroundColor: "#FF965C",
    justifyContent: "center",
    alignItems: "center",
    height: 50,
    marginLeft: 10,
    borderRadius: 8,
  },
  addButtonText: {
    color: "white",
    fontSize: 18,
  },
  categoriesContainer: {
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "flex-start",
    alignItems: "center",
  },
  categoryBox: {
    flexDirection: "row",
    justifyContent: "space-between",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 20,
    padding: 8,
    marginRight: 10,
    marginBottom: 10,
  },
  centerImage: {
    width: 120,
    height: 120,
    marginBottom: 20,
  },
  questionText: {
    fontSize: 24,
  },
  moveText: {
    color: "#7B7B7B",
  },
  center: { justifyContent: "center", alignItems: "center" },
  categoryGroup: { marginBottom: 20 },
});
