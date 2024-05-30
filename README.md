#만드는 순서
domain -> user // 만들고 싶은 데이터베이스설계
dto -> UserDTO // 입력받고 싶은 데이터 값들 
     user table 이면 안에 mid, mpw, mname 이 있다면 그렇게 맵핑해줄만한걸 해주는거다.
repository - >

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByMidAndMpw(String mid, String mpw);
    }  만드는 이유는 데이터베이스에 올리는 기능을 쓰기위해 JpaRepository의 기능을 입력받기 위하여
    <User, Long> 내가 입력할 타입 받아올 타입 지금은 User타입을 쓰고 Long으로 받아올거다 라는 뜻

service ->
  public interface UserService {
    Long register(UserDTO userDTO);
    UserDTO login(String mid, String mpw);
} 서비스는 인터페이스로 받는다 내가 쓸 기능들을 여기서 정의만 해놓는다.

serviceImple ->

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{ //세이브하기 위해 사용

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public Long register(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Long id = userRepository.save(user).getId();
        return id;
    }
이제 정의했던 기능들을 사용할 수 있게 만들어준다.
@RequiredArgsConstructor은 
원래는 final ModleMapper 은 public void UserServiceImp해서 this.modelMapper이런식으로 해줘야하는데 대신 정의해주는 기능

이제 사용을 위한 Controller

@Controller //컨트롤러다 라는걸 알려주는거
@RequestMapping("/users") //기본주소를 앞에 맵핑해줌 -> localhost:8080/users/~~~
@Log4j2 //로그 확인할려고 log.info("~~")
@RequiredArgsConstructor //같이 정의해주는 기능
public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/register")
    public void registerGet() { //사이트에 접속했을때 get했을때 쓰기위해 getmapping
    }

    @PostMapping("/register") //html register에서 submit해온걸 받기위해 포스트 매핑
    public String registerPost(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){ // bindingResult는 에러를 처리해주는 친구이다. 만약 에러에 뭐가 있다면~
          redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors()); 
          // flash는 한번만 사용한다는 거임 한번하고나면 사라짐
          // register에 에러를 넣어준다는 거다.
          return "redirect:/users/register";
        }

        Long id = userService.register(userDTO); //Long id를 받기위해 register를 처리한다.
        redirectAttributes.addFlashAttribute("message", "User registered successfully");
        return "redirect:/board/list"; //이페이지로 간다<<
    }
