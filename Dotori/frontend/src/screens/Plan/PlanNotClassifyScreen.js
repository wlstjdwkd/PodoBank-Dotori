import React, { useState, useMemo, useEffect } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  FlatList,
  Modal,
} from "react-native";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import { useIsFocused } from "@react-navigation/native";
import { unclassifiedList, unClassifiedUpdate } from "../../apis/planapi";

export default function PlanNotClassifyScreen({ navigation, route }) {
  // 토큰
  const grantType = useSelector((state) => state.user.grantType);
  const accessToken = useSelector((state) => state.user.accessToken);
  const refreshToken = useSelector((state) => state.user.refreshToken);
  const dispatch = useDispatch();
  const isFocused = useIsFocused();
  const planSeq = route.params.planSeq;

  // 그 외
  const accountName = route.params.accountName;
  // TODO: 서버에서 데이터를 가져와 아래 변수들을 설정하세요
  const samepleData2 = [
    {
      planDetailSeq: 1,
      categoryName: "냥냥이 식비",
      paymentSeq: 2,
      paymentName: "냥냥밥",
      paymentPrice: 30000,
      paymentDate: "2023-08-12 16:30",
    },
    {
      planDetailSeq: 2,
      categoryName: "배달비",
      paymentSeq: 3,
      paymentName: "요기요",
      paymentPrice: 30030,
      paymentDate: "2023-08-13 15:30",
    },
    {
      planDetailSeq: 2,
      categoryName: "배달비",
      paymentSeq: 4,
      paymentName: "배달의 민족",
      paymentPrice: 32030,
      paymentDate: "2023-08-15 15:30",
    },
  ];
  const [data, setData] = useState(samepleData2); // 이 부분 추가

  const [categoryMapping, setCategoryMapping] = useState({});

  const doUnClassifiedList = async () => {
    try {
      const response = await unclassifiedList(planSeq, accessToken, grantType);
      if (response.status === 200) {
        setPlanInfo(response.data);
      } else {
        console.log("계획 정보 조회 실패", response.status);
      }
    } catch (error) {
      console.error("오류 발생 : 계획 정보 조회 실패:", error);
    }
  };

  const doUnClassifiedUpdate = async () => {
    try {
      const response = await unClassifiedUpdate(
        data,
        planSeq,
        accessToken,
        grantType
      );
      if (response.status === 200) {
        setData(response.data);
      } else {
        console.log("계획 정보 조회 실패", response.status);
      }
    } catch (error) {
      console.error("오류 발생 : 계획 정보 조회 실패:", error);
    }
  };

  useEffect(() => {
    // 서버로부터 데이터를 가져오는 코드가 들어갈 위치입니다.
    // 예시로는, samepleData2를 사용합니다.
    if (isFocused) {
      //doUnClassifiedList();
    }
    const mapping = {};
    samepleData2.forEach((item) => {
      if (!mapping[item.categoryName]) {
        mapping[item.categoryName] = item.planDetailSeq;
      }
    });
    setCategoryMapping(mapping);
  }, [isFocused]); // 이 빈 배열은 이 useEffect가 컴포넌트가 마운트될 때만 실행되게 합니다.

  const onCategoryChange = (paymentSeq, newCategory) => {
    const newPlanDetailSeq = categoryMapping[newCategory];

    const newData = data.map((item) =>
      item.paymentSeq === paymentSeq
        ? {
            ...item,
            categoryName: newCategory,
            planDetailSeq: newPlanDetailSeq,
          }
        : item
    );
    setData(newData);
    console.log(newData);
  };

  // uniqueCategories 정의
  const uniqueCategories = useMemo(() => {
    return [...new Set(samepleData2.map((item) => item.categoryName))];
  }, [samepleData2]);

  const formatNumber = (num) =>
    num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");

  const handleNextButton = () => {
    // doPlanNewRegister();
    navigation.navigate("PlanMainScreen", {
      accountSeq: planInfo.accountSeq,
    });
  };
  // 기존의 sampleData 대신 sampleData2 사용
  const [selectedCategoryName, setSelectedCategoryName] = useState(
    samepleData2[0]?.categoryName || null
  );

  // 적절한 카테고리에 해당하는 아이템들을 얻기 위한 함수 변경
  const getCategoryItems = () =>
    data.filter((item) => item.categoryName === selectedCategoryName) || [];

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="미분류 항목"
        cancelNavi="MainPageScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <Text style={styles.accountName}>{accountName}</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {uniqueCategories.map((category) => (
            <TouchableOpacity
              key={category}
              onPress={() => setSelectedCategoryName(category)}
              style={
                selectedCategoryName === category
                  ? styles.selectedGroupName
                  : styles.groupName
              }
            >
              <Text
                style={
                  selectedCategoryName === category
                    ? styles.selectedGroupText
                    : styles.groupText
                }
              >
                {category}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
        <View style={styles.divider} />
        {/* // FlatList 부분 변경 */}
        <FlatList
          data={getCategoryItems()}
          renderItem={({ item }) => (
            <View style={styles.itemContainer}>
              <View style={styles.itemLeftContainer}>
                <View style={styles.transactionInfo}>
                  <Text style={styles.transactionDetail}>
                    {item.paymentName}
                  </Text>
                  <CategoryBox
                    item={item}
                    onCategoryChange={onCategoryChange}
                    uniqueCategories={uniqueCategories}
                  />
                </View>

                <View style={styles.transactionInfo}>
                  <Text style={styles.transactionAt}>{item.paymentDate}</Text>
                  <Text style={styles.amount}>
                    {formatNumber(item.paymentPrice)}원
                  </Text>
                </View>
              </View>
            </View>
          )}
          keyExtractor={(item, index) => index.toString()}
        />
      </View>
      <TouchableOpacity style={styles.button} onPress={handleNextButton}>
        <Text style={styles.buttonText}>완료</Text>
      </TouchableOpacity>
    </View>
  );
}

function CategoryBox({ item, onCategoryChange, uniqueCategories }) {
  const [modalVisible, setModalVisible] = useState(false);

  return (
    <>
      <TouchableOpacity
        style={styles.categoryBox}
        onPress={() => setModalVisible(true)}
      >
        <Text style={styles.categoryText}>{item.categoryName}</Text>
      </TouchableOpacity>

      {modalVisible && (
        <Modal
          transparent={true}
          animationType="slide"
          visible={modalVisible}
          onRequestClose={() => setModalVisible(false)}
        >
          <View style={styles.modalOverlay}>
            <View style={styles.modalContainer}>
              <View style={styles.modalHeader}>
                <Text style={styles.modalHeaderText}>카테고리 변경</Text>
                <TouchableOpacity onPress={() => setModalVisible(false)}>
                  <Text style={styles.modalCloseText}>X</Text>
                </TouchableOpacity>
              </View>

              <ScrollView contentContainerStyle={styles.modalContent}>
                {uniqueCategories.map((category) => (
                  <TouchableOpacity
                    key={category}
                    style={styles.modalItem}
                    onPress={() => {
                      setModalVisible(false);
                      onCategoryChange(item.paymentSeq, category);
                    }}
                  >
                    <Text style={styles.modalItemText}>{category}</Text>
                  </TouchableOpacity>
                ))}
              </ScrollView>
            </View>
          </View>
        </Modal>
      )}
    </>
  );
}

const styles = StyleSheet.create({
  modalOverlay: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "rgba(0,0,0,0.5)",
  },
  modalContainer: {
    width: "80%",
    maxHeight: "50%", // 화면 중간까지 올라오게 설정
    backgroundColor: "white",
    borderRadius: 10,
    padding: 20,
    overflow: "hidden",
  },
  modalHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 10,
  },
  modalHeaderText: {
    fontSize: 16,
    fontWeight: "bold",
  },
  modalCloseText: {
    fontSize: 16,
    fontWeight: "bold",
  },
  modalContent: {
    alignItems: "center",
  },
  modalItem: {
    width: "100%",
    backgroundColor: "#727070",
    borderRadius: 10,
    marginBottom: 10,
    padding: 10,
  },
  modalItemText: {
    color: "white",
    fontSize: 14,
  },
  container: {
    flex: 1,
    // alignItems: "center",
    backgroundColor: "white",
    padding: 16,
  },
  innerContainer: {
    // alignItems: "center",
  },
  accountName: {
    fontWeight: "bold",
    alignSelf: "flex-start",
    fontSize: 24,
    marginLeft: 16,
    marginTop: 20,
    marginBottom: 20,
  },
  groupName: {
    padding: 10,
  },

  selectedGroupName: {
    borderBottomWidth: 5,
    borderBottomColor: "#FF965C",
    padding: 10,
  },
  groupText: {
    color: "#727070",
  },
  selectedGroupText: {
    color: "#FF965C",
  },
  divider: {
    height: 1,
    backgroundColor: "#F5F5F5",
    width: "100%",
    marginVertical: 10,
    marginBottom: 30,
  },
  itemContainer: {
    flexDirection: "row",
    // justifyContent: "space-between",
    alignItems: "center",
    padding: 10,
    borderColor: "#E3E3E3",
    borderWidth: 1,
    borderRadius: 15,
    marginVertical: 5,
    marginLeft: 20,

    width: "90%",

    elevation: 5,
    backgroundColor: "white",
  },
  itemLeftContainer: {
    flex: 1,
    justifyContent: "space-between",
    marginLeft: 10,
    marginRight: 10,
  },
  transactionInfo: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 5,
  },
  transactionDetail: {
    flex: 1,
    fontWeight: "bold",
    marginBottom: 10,
  },
  categoryBox: {
    backgroundColor: "#F2F5F8",
    borderRadius: 10,
    padding: 5,
    // marginTop: -20,
    paddingTop: 6,
    marginBottom: 3,
  },
  transactionAt: {
    color: "#7D7D7D",
    fontSize: 12,
  },
  amount: {
    textAlign: "right",
    fontWeight: "bold",
  },
  categoryText: {
    fontSize: 10,
  },
  button: {
    height: 50,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 400,
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
});
