(ns scheje.library)

(def scheje-version "0.2.16")

(def
  root-env
  {
   'caar        '(lambda (x) (car (car x)))                    
   'cadr        '(lambda (x) (car (cdr x)))                    
   'cdar        '(lambda (x) (cdr (car x)))                    
   'cddr        '(lambda (x) (cdr (cdr x)))                    
   'caaar       '(lambda (x) (car (car (car x))))              
   'caadr       '(lambda (x) (car (car (cdr x))))              
   'cadar       '(lambda (x) (car (cdr (car x))))              
   'caddr       '(lambda (x) (car (cdr (cdr x))))              
   'cdaar       '(lambda (x) (cdr (car (car x))))              
   'cdadr       '(lambda (x) (cdr (car (cdr x))))              
   'cddar       '(lambda (x) (cdr (cdr (car x))))              
   'cdddr       '(lambda (x) (cdr (cdr (cdr x))))              
   'caaaar      '(lambda (x) (car (car (car (car x)))))        
   'caaadr      '(lambda (x) (car (car (car (cdr x)))))        
   'caadar      '(lambda (x) (car (car (cdr (car x)))))        
   'caaddr      '(lambda (x) (car (car (cdr (cdr x)))))        
   'cadaar      '(lambda (x) (car (cdr (car (car x)))))        
   'cadadr      '(lambda (x) (car (cdr (car (cdr x)))))        
   'caddar      '(lambda (x) (car (cdr (cdr (car x)))))        
   'cadddr      '(lambda (x) (car (cdr (cdr (cdr x)))))        
   'cdaaar      '(lambda (x) (cdr (car (car (car x)))))        
   'cdaadr      '(lambda (x) (cdr (car (car (cdr x)))))        
   'cdadar      '(lambda (x) (cdr (car (cdr (car x)))))        
   'cdaddr      '(lambda (x) (cdr (car (cdr (cdr x)))))        
   'cddaar      '(lambda (x) (cdr (cdr (car (car x)))))        
   'cddadr      '(lambda (x) (cdr (cdr (car (cdr x)))))        
   'cdddar      '(lambda (x) (cdr (cdr (cdr (car x)))))        
   'cddddr      '(lambda (x) (cdr (cdr (cdr (cdr x)))))    
   
   'zero?       '(lambda(x)(zero? x))
   'number? '(lambda(x)(number? x))
   'symbol?     '(lambda(x) (symbol? x))
   'assoc '(lambda (k l)
                   (cond ((eq? l (quote ())) false)
                         ((eq? (car (car l)) k) (car l))
                         (else (assoc k (cdr l)))))
   'alist-cons '(lambda (key datum alist)
                        (cons (cons key (list  datum)) alist))
   'true true
   'vector-ref '(lambda(v i)(vector-ref v i))
   'list '(lambda x x)
   'false false
   'else true
   'vector vector
   'vector? 'vector?
   :keywords ['vector 'vector? '=> 'not 'pair? 'length 'quote
              'unquote 'unquote-splicing 'quasiquote '= 'list 'cond 'lambda 'if 'else
              'display 'cons 'car 'cdr 'null? 'atom? '+ '- 'eq? '< '<= '> '>= '/ '*
              'false 'true 'else 'caar 'cadr 'cdar  
              'cddr 'caaar 'caadr 'cadar 'caddr 'cdaar 'cdadr 'cddar 'cdddr 
              'caaaar 'caaadr 'caadar 'caaddr 'cadaar 'cadadr 'caddar 'cadddr
              'cdaaar 'cdaadr 'cdadar 'cdaddr 'cddaar 'cddadr 'cdddar 'cddddr]
   
   :syntax ['{:name let, :literals (),
              :rules (((let () body ... ) ((lambda() body ...)))
                      ((let ((var expr) ...) body ...) ((lambda (var ...) body ...) expr ...))
                      ((let let-name ((var expr)...) body ... ) (letrec ((let-name (lambda (var ...) body ...)))
                                                                        (let-name expr ...))))}

            '{:name letrec, :literals (),
              :rules (((letrec bindings body) (let bindings body)))}
            
            '{:name let*, :literals (),
              :rules (((let* () body ...) (let () body ...))
                      ((let* (binding) body ...) (let (binding) body ...))
                      ((let* (binding bnext ...) body ...) (let (binding) (let* (bnext ...) body ...))))}

            '{:name letrec*, :literals (),
              :rules (((letrec* bindings body) (let* bindings body)))}
            
            '{:name and, :literals (),
              :rules (((and x) x) ((and) true)
                      ((and x y ...) (if x (and y ...) false)))}
            
            '{:name or, :literals (),
              :rules (((or) true)
                      ((or x) x)
                      ((or x y ...) (if x true (or y ...))))}]})
