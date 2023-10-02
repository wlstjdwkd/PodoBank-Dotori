import React, { useEffect, useRef, useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Image,
  FlatList,
  Dimensions,
  ScrollView,
  PanResponder,
  Alert,
  Modal,
  Button,
  Animated,
} from "react-native";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import { planCategoryUsingSpot } from "../../apis/planapi"
import { FontAwesome } from '@expo/vector-icons';

const categorys = [
  {
    categorySeq: "1",
    categoryTitle: "옷(겨울)",
    categoryList: [
      "마왕족발",
      "김치찜은 못참치",
      "최각루",
      "피자가좋다",
      "세찜",
      "치킨과피자가같이 이게",
      "내가닭이다",
      "52는맛있지",
      "최코다리"
    ],
    newCategoryList: ["신촌 등갈비찜", '우정양개탕', '다이소다이소'],
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

export default function CategorySettingScreen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  const [categorySeq, setCategorySeq] = useState(route.params.categorySeq);
  const [isDeleteModalVisible, setDeleteModalVisible] = useState(false);

  
  
  
  // categorySeq와 일치하는 category 데이터를 찾습니다.
  const [category, setCategory] = useState(categorys.find((item) => item.categorySeq === categorySeq))

  const [windowWidth, setWindowWidth] = useState(Dimensions.get('window').width);
  const [windowHeight, setWindowHeight] = useState(Dimensions.get('window').height);
  const [trashStyles, setTrashStyles] = useState(styles.circle);

  const [panValues, setPanValues] = useState(
    category.categoryList.map(() => new Animated.ValueXY())
  );

  const [newCategoryPanValues, setNewCategoryPanValues] = useState(
    category.newCategoryList.map(() => new Animated.ValueXY())
  );

  const panResponders = category.categoryList.map((item, index) =>
    PanResponder.create({
      onMoveShouldSetPanResponder: () => true,
      onPanResponderMove: (evt, gestureState) => {
        Animated.event(
          [
            null,
            {
              dx: panValues[index].x,
              dy: panValues[index].y,
            },
          ],
          { useNativeDriver: false }
        )(evt, gestureState);

        if (isDropZone(gestureState, index)) {
          console.log(gestureState)
          setTrashStyles(styles.circle2);
        } else {
          setTrashStyles(styles.circle);
        }
      },
      onPanResponderRelease: (evt, gestureState) => {
        if (isDropZone(gestureState, index)) {
          const updatedCategoryList = [...category.categoryList];
          updatedCategoryList.splice(index, 1);
          setCategory((prevCategory) => ({
            ...prevCategory,
            categoryList: updatedCategoryList,
          }));
        }else{
          Animated.spring(panValues[index], {
            toValue: { x: 0, y: 0 },
            useNativeDriver: false,
          }).start();
        }
        setTrashStyles(styles.circle);
      },
    })
  );

  const newCategoryPanResponders = category.newCategoryList.map((item, index) =>
    PanResponder.create({
      onMoveShouldSetPanResponder: () => true,
      onPanResponderMove: (evt, gestureState) => {
        Animated.event(
          [
            null,
            {
              dx: newCategoryPanValues[index].x,
              dy: newCategoryPanValues[index].y,
            },
          ],
          { useNativeDriver: false }
        )(evt, gestureState);

        if (isDropZone(gestureState, index)) {
          setTrashStyles(styles.circle2);
        } else {
          setTrashStyles(styles.circle);
        }
      },
      onPanResponderRelease: (evt, gestureState) => {
        if (isDropZone(gestureState, index)) {
          // Alert.alert('작동', '작동됨');
          const updatedNewCategoryList = [...category.newCategoryList];
          updatedNewCategoryList.splice(index, 1);
          setCategory((prevCategory) => ({
            ...prevCategory,
            newCategoryList: updatedNewCategoryList,
          }));
        }else{
          Animated.spring(newCategoryPanValues[index], {
            toValue: { x: 0, y: 0 },
            useNativeDriver: false,
          }).start();
        }

        setTrashStyles(styles.circle);
      },
    })
  );

  const isDropZone = (gestureState, boxId) => {
    const box2X = windowWidth / 2;
    const box2Y = windowHeight - 50;
    const box2Width = 30;
    const box2Height = 30;

    const box1X = gestureState.moveX;
    const box1Y = gestureState.moveY;

    const isInsideDropZone =
      box1X >= box2X - box2Width &&
      box1X <= box2X + box2Width &&
      box1Y >= box2Y - box2Height &&
      box1Y <= box2Y + box2Height;

    return isInsideDropZone;
  };


  // 카테고리 사용처 목록 가져오기 함수
  const doPlanCategoryUsingSpot = async () => {
    try{
      const response = await planCategoryUsingSpot(categorySeq, accessToken, grantType)
      if(response.status===200){
        console.log("카테고리 사용처 목록 데이터:",response.data)
        // setCategory(response.data)
        console.log('카테고리 사용처 목록 가져오기 성공') 
      }else{
        console.log('카테고리 사용처 목록 가져오기 실패',response.status) 
      } 
    }catch(error){
      console.log('카테고리 사용처 목록 가져오기 실패',error) 
    }
  }


  

  useEffect(()=>{
    doPlanCategoryUsingSpot()
  },[])

  return (
    <View style={styles.container}>
      <HeaderComponent title="카테고리 세부 내용"></HeaderComponent>

      <View style={styles.topContainer}>
        {/* 페이지 상단 좌측에 categoryTitle 출력 */}
        <View style={styles.categoryTitleContainer}>
          <Text style={styles.categoryTitle}>{category.categoryTitle}</Text>
          <Text style={styles.categoryTitleExplain}>
            자주 사용하는 사용처를 분류해주세요.
          </Text>
        </View>
        <Image
          style={styles.checkIcon}
          source={require("../../assets/images/Hamster/CheckHamster.png")}
        />
      </View>

      {/* 자주 사용하는 사용처 */}
      <View style={styles.categoryGroup}>
        <Text style={styles.categoryGroupText}>자주 사용하는 사용처</Text>
      </View>
      {/* 카테고리 목록 */}
      <View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
        {category.categoryList.map((item, index) => (
          <Animated.View
            key={index}
            style={[
              styles.categoryItem,
              { transform: panValues[index].getTranslateTransform() },
            ]}
            {...panResponders[index].panHandlers}
          >
            <Text style={styles.categoryItemText}>{item}</Text>
          </Animated.View>
        ))}
      </View>
      {/* 구분선 */}
      <View style={styles.separator}></View>
      {/* 신규 사용처 */}
      <View style={styles.categoryGroup}>
        <Text style={styles.categoryGroupText}>신규 사용처</Text>
      </View>
      {/* 신규 카테고리 목록 */}
      <View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
        {category.newCategoryList.map((item, index) => (
          <Animated.View
            key={index}
            style={[
              styles.categoryItem,
              {
                transform: newCategoryPanValues[index].getTranslateTransform(),
              },
            ]}
            {...newCategoryPanResponders[index].panHandlers}
          >
            <Text style={styles.categoryItemText}>{item}</Text>
          </Animated.View>
        ))}
      </View>

      {/* 쓰레기통 */}
      <View style={[trashStyles, {position: 'absolute', alignSelf: 'center', }]}>
        <FontAwesome name="trash-o" size={50} color="white" />
      </View>

      {/* 모달 */}
      {/* <Modal
        visible={isDeleteModalVisible}
        transparent={true}
        animationType="slide"
      >
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: 'rgba(0, 0, 0, 0.5)' }}>
          <View style={{ backgroundColor: 'white', padding: 20, borderRadius: 10, alignItems: 'center' }}>
            <Text>{`"${item}"을(를) 삭제하시겠습니까?`}</Text>
            <Button title="예" onPress={onConfirmDelete} />
            <Button title="아니오" onPress={onCancelDelete} />
          </View>
        </View>
      </Modal> */}
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
    width: 20,
    height: 20,
  },
  topContainer: {
    flexDirection: "row",
    // alignContent: "place-between",
    marginLeft: "5%",
    // width: "90%",
    marginTop: 20,
    marginBottom: 50,
  },
  categoryTitleContainer: {
    flexDirection: "column",
  },
  categoryTitle: {
    fontSize: 20,
    fontWeight: "bold",

    flex: 1,
    textAlign: "left",
    marginLeft: 8, // categoryTitle을 뒤에 추가
    marginTop: 70,
    marginBottom: -10,
  },
  categoryTitleExplain: {
    fontSize: 10,
    flex: 1,
    textAlign: "left",
    marginLeft: 8, // categoryTitle을 뒤에 추가
    color: "#7B7B7B",
  },
  categoryGroup: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 20,
  },
  categoryGroupText: {
    fontSize: 20,
    fontWeight: "bold",
    marginLeft: "5%",
    marginBottom: 10,
  },
  checkIcon: {
    width: 150,
    height: 150,
    marginLeft: 10,
  },
  categoryList: {
    flexDirection: "column", // 세로로 배치
    alignSelf: "left",
  },
  categoryItem: {
    alignItems: "center",
    padding: 4,
    marginBottom: 8,
    marginHorizontal: 8, // 좌우 여백 추가
    borderWidth: 1, // 테두리 추가
    borderColor: "#7B7B7B", // 테두리 색상
    borderRadius: 20, // 테두리 둥글게 설정
  },
  categoryItemText: {
    fontSize: 16,
    marginHorizontal: 5,
  },
  separator: {
    height: 1,
    backgroundColor: "#BAC0CA", // 구분선 색상 변경
    marginVertical: 40,
  },
  circle: {
    width: 60, // 원의 너비
    height: 60, // 원의 높이
    borderRadius: 50, // 반지름의 절반 크기로 설정하여 원 모양으로 만듭니다.
    borderWidth: 1, // 테두리 두께
    borderColor: "#7B7B7B", // 테두리 색상
    backgroundColor: "#7B7B7B",
    padding: 0, // 원 내부에 패딩 적용
    justifyContent: 'center', // 원 내부 컨텐츠를 가운데 정렬
    alignItems: 'center', // 원 내부 컨텐츠를 가운데 정렬
    bottom: 50
  },
  circle2: {
    width: 80, // 원의 너비
    height: 80, // 원의 높이
    borderRadius: 50, // 반지름의 절반 크기로 설정하여 원 모양으로 만듭니다.
    borderWidth: 1, // 테두리 두께
    borderColor: "black", // 테두리 색상
    backgroundColor: "black",
    padding: 0, // 원 내부에 패딩 적용
    justifyContent: 'center', // 원 내부 컨텐츠를 가운데 정렬
    alignItems: 'center', // 원 내부 컨텐츠를 가운데 정렬
    bottom: 40
  },
});
